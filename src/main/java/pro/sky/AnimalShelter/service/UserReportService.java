package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.*;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.enums.UserReportStates;
import pro.sky.AnimalShelter.repository.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.enums.UserReportStates.*;

/**
 * Сервис для управления отчетами о пользователях.
 * Этот сервис предоставляет методы для сохранения данных отчетов и фотографий,
 * связанных с пользователями и их питомцами (кошками и собаками).
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserReportService {

    /**
     * Максимальный размер фотографии в байтах, который допускается при сохранении.
     */
    public static final int MAX_SIZE_IN_BYTES = 15_000;

    /**
     * Репозиторий для работы с отчетами о кошках.
     */
    private final CatReportRepository catReportRepository;

    /**
     * Репозиторий для работы с отчетами о собаках.
     */
    private final DogReportRepository dogReportRepository;

    /**
     * Репозиторий для работы с пользователями.
     */
    private final UserRepository userRepository;

    /**
     * Сервис для управления состоянием чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Сервис для управления состоянием отчетов о пользователях.
     */
    private final UserReportStateService userReportStateService;

    /**
     * Сервис для взаимодействия с Telegram Bot API.
     */
    private final TelegramBot telegramBot;

    /**
     * Репозиторий для работы с чатами.
     */
    private final ChatRepository chatRepository;

    /**
     * Репозиторий для работы с фотографиями кошек.
     */
    private final CatPhotoRepository catPhotoRepository;

    /**
     * Репозиторий для работы с фотографиями собак.
     */
    private final DogPhotoRepository dogPhotoRepository;

    /**
     * Репозиторий для работы с данными о кошках.
     */
    private final CatRepository catRepository;

    /**
     * Репозиторий для работы с данными о собаках.
     */
    private final DogRepository dogRepository;

    /**
     * Метод для сохранения данных отчета о пользователе.
     * Этот метод сохраняет текстовые данные отчета в зависимости от его типа (Поведение, Рацион, Общее самочувствие).
     *
     * @param chatId Идентификатор чата пользователя.
     * @param text   Текстовые данные отчета.
     * @param state  Тип отчета (Поведение, Рацион, Общее самочувствие).
     */
    public void saveReportData(Long chatId, String text, UserReportStates state) {
        log.info("saveReportData method was invoked");
        Optional<Chat> byChatId = chatRepository.findByChatId(chatId);
        if (byChatId.isEmpty()) {
            return;
        }

        if (state != BEHAVIOR && state != RATION && state != WELL_BEING) {
            log.warn("Invalid report type: {}", state);
            return;
        }

        BotCommand lastStateCatOrDogByChatId = chatStateService.getLastStateCatOrDogByChatId(chatId);
        userRepository.findByChatId(byChatId.get().getId()).ifPresentOrElse(user -> {
            Optional<Cat> foundCat = catRepository.findByUserId(user.getId());
            Optional<Dog> foundDog = dogRepository.findByUserId(user.getId());

            if ((foundDog.isPresent() && lastStateCatOrDogByChatId == DOG) ||
                    (foundCat.isPresent() && lastStateCatOrDogByChatId == CAT) ||
                    (foundCat.isPresent() && foundDog.isPresent() && lastStateCatOrDogByChatId != null)) {
                Object report = lastStateCatOrDogByChatId == DOG
                        ? dogReportRepository.findByUserId(user.getId()).orElseGet(DogReport::new)
                        : catReportRepository.findByUserId(user.getId()).orElseGet(CatReport::new);

                if (report instanceof DogReport) {
                    DogReport dogReport = (DogReport) report;
                    dogReport.setUser(user);
                    dogReport.setDog(foundDog.get());
                    if (state == BEHAVIOR) {
                        dogReport.setBehavior(text);
                        dogReport.setUpdated(LocalDateTime.now());
                        userReportStateService.clearUserReportStates(chatId);
                        telegramBot.execute(new SendMessage(chatId,
                                "Изменения в поведении собаки успешно сохранены в отчёт\n" +
                                        "Возврат в предыдущее меню (/back)\n" +
                                        "Выключить бота (/stop)"));
                    } else if (state == RATION) {
                        dogReport.setRation(text);
                        telegramBot.execute(new SendMessage(chatId, "Рацион собаки успешно сохранен в отчёт, введите общее самочувствие:"));
                        userReportStateService.updateUserReportState(chatId, WELL_BEING);
                    } else {
                        dogReport.setWellBeing(text);
                        telegramBot.execute(new SendMessage(chatId, "Общее самочувствие собаки успешно сохранено в отчёт, введите изменения в поведении:"));
                        userReportStateService.updateUserReportState(chatId, BEHAVIOR);
                    }
                    dogReport.setReportVerified(false);
                    dogReportRepository.save(dogReport);
                } else {
                    CatReport catReport = (CatReport) report;
                    catReport.setUser(user);
                    catReport.setCat(foundCat.get());
                    if (state == BEHAVIOR) {
                        catReport.setBehavior(text);
                        catReport.setUpdated(LocalDateTime.now());
                        userReportStateService.clearUserReportStates(chatId);
                        telegramBot.execute(new SendMessage(chatId,
                                "Изменения в поведении кошки успешно сохранены в отчёт\n" +
                                        "Возврат в предыдущее меню (/back)\n" +
                                        "Выключить бота (/stop)"));
                    } else if (state == RATION) {
                        catReport.setRation(text);
                        telegramBot.execute(new SendMessage(chatId, "Рацион кошки успешно сохранен в отчёт, введите общее самочувствие:"));
                        userReportStateService.updateUserReportState(chatId, WELL_BEING);
                    } else {
                        catReport.setWellBeing(text);
                        telegramBot.execute(new SendMessage(chatId, "Общее самочувствие кошки успешно сохранено в отчёт, введите изменения в поведении:"));
                        userReportStateService.updateUserReportState(chatId, BEHAVIOR);
                    }
                    catReport.setReportVerified(false);
                    catReportRepository.save(catReport);
                }
            } else {
                if (lastStateCatOrDogByChatId == DOG) {
                    telegramBot.execute(new SendMessage(chatId, "У вас нет собаки, нельзя отправлять отчет по собаке."));
                } else if (lastStateCatOrDogByChatId == CAT) {
                    telegramBot.execute(new SendMessage(chatId, "У вас нет кошки, нельзя отправлять отчет по кошке."));
                } else {
                    telegramBot.execute(new SendMessage(chatId, "Непредвиденная ошибка, перезапустите телеграм бот (/stop)"));
                }
            }
        }, () -> telegramBot.execute(new SendMessage(chatId, "Пользователь не найден\n" +
                "Оставьте контактные данные")));
    }

    /**
     * Метод для сохранения фотографии в отчете о пользователе.
     * Этот метод сохраняет фотографию питомца пользователя в отчете в зависимости от его типа (кошка или собака).
     *
     * @param chatId    Идентификатор чата пользователя.
     * @param photoSize Фотография в формате PhotoSize.
     */
    public void savePhotoForReport(Long chatId, PhotoSize[] photoSize) {
        log.info("savePhotoForReport method was invoked");
        Optional<Chat> byChatId = chatRepository.findByChatId(chatId);
        if (byChatId.isEmpty()) {
            return;
        }

        if (photoSize.length == 0) {
            return;
        }

        BotCommand lastStateCatOrDogByChatId = chatStateService.getLastStateCatOrDogByChatId(chatId);
        userRepository.findByChatId(byChatId.get().getId()).ifPresentOrElse(user -> {
            Optional<Cat> foundCat = catRepository.findByUserId(user.getId());
            Optional<Dog> foundDog = dogRepository.findByUserId(user.getId());

            if ((foundDog.isPresent() && lastStateCatOrDogByChatId == DOG) ||
                    (foundCat.isPresent() && lastStateCatOrDogByChatId == CAT) ||
                    (foundCat.isPresent() && foundDog.isPresent() && lastStateCatOrDogByChatId != null)) {
                Object report = lastStateCatOrDogByChatId == DOG
                        ? dogReportRepository.findByUserId(user.getId()).orElseGet(DogReport::new)
                        : catReportRepository.findByUserId(user.getId()).orElseGet(CatReport::new);

                findPhotoWithinMaxSize(photoSize).ifPresentOrElse(photo -> {
                    byte[] data = photoSizeToBytes(photo);
                    if (data != null) {
                        String mediaType = new Tika().detect(data);
                        Long fileSize = photo.fileSize();

                        if (report instanceof DogReport) {
                            DogReport dogReport = (DogReport) report;
                            dogReport.setUser(user);
                            dogReport.setDog(foundDog.get());
                            dogReport.setDogPhoto(dogPhotoRepository.save(DogPhoto.builder()
                                    .data(data)
                                    .mediaType(mediaType)
                                    .fileSize(fileSize)
                                    .build()));
                            dogReportRepository.save(dogReport);
                            telegramBot.execute(new SendMessage(chatId, "Фотография собаки успешно сохранена в отчёт, введите рацион:"));
                            userReportStateService.updateUserReportState(chatId, RATION);
                        } else {
                            CatReport catReport = (CatReport) report;
                            catReport.setUser(user);
                            catReport.setCat(foundCat.get());
                            catReport.setCatPhoto(catPhotoRepository.save(CatPhoto.builder()
                                    .data(data)
                                    .mediaType(mediaType)
                                    .fileSize(fileSize)
                                    .build()));
                            catReportRepository.save(catReport);
                            telegramBot.execute(new SendMessage(chatId, "Фотография кошки успешно сохранена в отчёт, введите рацион:"));
                            userReportStateService.updateUserReportState(chatId, RATION);
                        }
                    } else {
                        telegramBot.execute(new SendMessage(chatId, "Ошибка загрузки фото"));
                    }
                }, () -> telegramBot.execute(new SendMessage(chatId, "Ошибка загрузки фото")));
            } else {
                if (lastStateCatOrDogByChatId == DOG) {
                    telegramBot.execute(new SendMessage(chatId, "У вас нет собаки, нельзя отправлять отчет по собаке."));
                } else if (lastStateCatOrDogByChatId == CAT) {
                    telegramBot.execute(new SendMessage(chatId, "У вас нет кошки, нельзя отправлять отчет по кошке."));
                } else {
                    telegramBot.execute(new SendMessage(chatId, "Непредвиденная ошибка, перезапустите телеграм бот (/stop)"));
                }
            }
        }, () -> telegramBot.execute(new SendMessage(chatId, "Пользователь не найден\n" +
                "Оставьте контактные данные")));
    }

    /**
     * Преобразует объект PhotoSize в массив байтов данных изображения.
     *
     * @param photoSize Фотография в формате PhotoSize.
     * @return Массив байтов данных изображения.
     */
    public byte[] photoSizeToBytes(PhotoSize photoSize) {
        log.info("photoSizeToBytes method was invoked");
        GetFileResponse getFileResponse = telegramBot.execute(new GetFile(photoSize.fileId()));
        if (getFileResponse.isOk()) {
            try {
                return telegramBot.getFileContent(getFileResponse.file());
            } catch (IOException e) {
                log.error("Error converting file to byte array");
            }
        }
        return null;
    }

    /**
     * Находит фотографию в массиве фотографий, которая удовлетворяет максимальному размеру.
     *
     * @param photoSizes Массив фотографий в формате PhotoSize.
     * @return Optional объект с фотографией, удовлетворяющей максимальному размеру.
     */
    public Optional<PhotoSize> findPhotoWithinMaxSize(PhotoSize[] photoSizes) {
        log.info("findPhotoWithinMaxSize method was invoked");
        return Arrays.stream(photoSizes)
                .filter(photoSize -> photoSize.fileSize() <= UserReportService.MAX_SIZE_IN_BYTES)
                .reduce((first, second) -> second)
                .or(() -> (photoSizes.length > 0) ? Optional.of(photoSizes[0]) : Optional.empty());
    }
}
