package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.CatReport;
import pro.sky.AnimalShelter.entity.DogReport;
import pro.sky.AnimalShelter.entity.VolunteerInfoCat;
import pro.sky.AnimalShelter.entity.VolunteerInfoDog;
import pro.sky.AnimalShelter.enums.CheckUserReportStates;
import pro.sky.AnimalShelter.repository.CatReportRepository;
import pro.sky.AnimalShelter.repository.DogReportRepository;
import pro.sky.AnimalShelter.repository.VolunteerInfoCatRepository;
import pro.sky.AnimalShelter.repository.VolunteerInfoDogRepository;

import java.util.List;
import java.util.stream.Collectors;

import static pro.sky.AnimalShelter.enums.CheckUserReportStates.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.*;

/**
 * Сервис для проверки отчетов от усыновителей.
 * Этот сервис предоставляет методы для просмотра и оценки качества
 * заполнения отчетов от усыновителей кошек и собак.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CheckUserReportService {

    /**
     * Сервис для управления состоянием просмотра и проверки отчета.
     */
    private final CheckUserReportStateService checkUserReportStateService;

    /**
     * Репозиторий для работы с информацией по испытательному сроку усыновителей кошек.
     */
    private final VolunteerInfoCatRepository volunteerInfoCatRepository;

    /**
     * Репозиторий для работы с информацией по испытательному сроку усыновителей собак.
     */
    private final VolunteerInfoDogRepository volunteerInfoDogRepository;

    /**
     * Репозиторий для работы с отчетами о кошках.
     */
    private final CatReportRepository catReportRepository;

    /**
     * Репозиторий для работы с отчетами о собаках.
     */
    private final DogReportRepository dogReportRepository;

    /**
     * Сервис для взаимодействия с Telegram Bot API.
     */
    private final TelegramBot telegramBot;

    /**
     * Вызов метода determinateAndSetCheckReportState без сообщения от волонтера и с нулевым состоянием проверки отчета.
     * Необходимо для перехода в первое состояние проверки отчета из CheckReportCommandHandler
     *
     * @param chatId Идентификатор чата волонтера.
     */
    public void init(Long chatId) {
        determinateAndSetCheckReportState(chatId, null, null);
    }

    /**
     * Метод перехода по состояниям проверки и оценки отчета от юзера
     *
     * @param chatId                      Идентификатор чата волонтера.
     * @param messageText                 текст сообщения от волонтера.
     * @param checkUserReportCurrentState Очередь состояний проверки и оценки отчета
     */
    public void determinateAndSetCheckReportState(Long chatId, String messageText, CheckUserReportStates checkUserReportCurrentState) {

        if (checkUserReportCurrentState == null) {
            getAllReports(chatId);
            checkUserReportStateService.updateCheckUserReportState(chatId, ALL_REPORTS);
            return;
        }
        if (checkUserReportCurrentState == ALL_REPORTS) {
            viewReport(chatId, messageText);
            checkUserReportStateService.updateCheckUserReportState(chatId, VIEW_REPORT);
            return;
        }
        if (checkUserReportCurrentState == VIEW_REPORT) {
            evaluateReport(chatId, messageText);
            checkUserReportStateService.updateCheckUserReportState(chatId, EVALUATE_REPORT);
        }
    }

    /**
     * Метод получения всех отчетов
     *
     * @param volunteerChatId Идентификатор чата волонтера.
     */
    protected void getAllReports(Long volunteerChatId) {
        getAllCatReports(volunteerChatId);
        getAllDogReports(volunteerChatId);
    }

    /**
     * Метод для отображения отчета от усыновителя в чате волонтера
     *
     * @param volunteerChatId Идентификатор чата волонтера.
     * @param messageText     текст сообщения от волонтера.
     */
    protected void viewReport(Long volunteerChatId, String messageText) {
        messageText = messageText.replaceAll("/", "");
        String[] petReportData = messageText.split("_");
        if (petReportData.length != 2) {
            telegramBot.execute(new SendMessage(volunteerChatId, DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT));
            return;
        }
        try {
            Long petReportId = Long.parseLong(petReportData[1].replaceAll("\\s+", ""));
            if (petReportData[0].equals("cat")) {
                viewCatReport(volunteerChatId, petReportId);
            } else if (petReportData[0].equals("dog")) {
                viewDogReport(volunteerChatId, petReportId);
            } else telegramBot.execute(new SendMessage(volunteerChatId,
                    "Такого животного я пока не знаю.\n" + DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT));
        } catch (NumberFormatException e) {
            telegramBot.execute(new SendMessage(volunteerChatId, DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT));
        }
    }

    /**
     * Метод для отображения отчета от усыновителя кошки в чате волонтера
     *
     * @param volunteerChatId Идентификатор чата волонтера.
     * @param catReportId     Идентификатор отчета по кошке.
     */
    protected void viewCatReport(Long volunteerChatId, Long catReportId) {
        if(catReportRepository.findById(catReportId).isPresent()) {
            CatReport catReport = catReportRepository.findById(catReportId).get();
            telegramBot.execute(new SendMessage(volunteerChatId,
                    "Отчет с идентификатором " + catReportId + " обновлен " + catReport.getUpdated()));
            telegramBot.execute(new SendPhoto(volunteerChatId, catReport.getCatPhoto().getData()));
            telegramBot.execute(new SendMessage(volunteerChatId,
                    "РАЦИОН: \n" +
                            catReport.getRation() +
                            "\n" +
                            "\n" +
                            "ОБЩЕЕ САМОЧУВСТВИЕ:\n" +
                            catReport.getWellBeing() +
                            "\n" +
                            "\n" +
                            "ИЗМЕНЕНИЯ В ПОВЕДЕНИИ:\n" +
                            catReport.getBehavior()));
            telegramBot.execute(new SendMessage(volunteerChatId,
                    CAT_REPORT_EVALUATION_TEXT +
                            "/5_cat_" + catReportId + RATING_EQUALS_FIVE_TEXT +
                            "/4_cat_" + catReportId + RATING_EQUALS_FOUR_TEXT +
                            "/3_cat_" + catReportId + RATING_EQUALS_THREE_TEXT +
                            "/2_cat_" + catReportId + " и /1_cat_" + catReportId + RATING_EQUALS_TWO_OR_ONE_TEXT
            ));
        } else telegramBot.execute(new SendMessage(volunteerChatId, CAT_REPORT_NOT_FOUND_BY_ID_TEXT
                + catReportId + WAY_BACK_TEXT));

    }

    /**
     * Метод для отображения отчета от усыновителя собаки в чате волонтера
     *
     * @param volunteerChatId Идентификатор чата волонтера.
     * @param dogReportId     Идентификатор отчета по собаке.
     */
    protected void viewDogReport(Long volunteerChatId, Long dogReportId) {
        if(dogReportRepository.findById(dogReportId).isPresent()) {
            DogReport dogReport = dogReportRepository.findById(dogReportId).get();
            telegramBot.execute(new SendMessage(volunteerChatId,
                    "Отчет с идентификатором " + dogReportId + " обновлен " + dogReport.getUpdated()));
            telegramBot.execute(new SendPhoto(volunteerChatId, dogReport.getDogPhoto().getData()));
            telegramBot.execute(new SendMessage(volunteerChatId,
                    "РАЦИОН:\n" +
                            dogReport.getRation() +
                            "\n" +
                            "\n" +
                            "ОБЩЕЕ САМОЧУВСТВИЕ:\n" +
                            dogReport.getWellBeing() +
                            "\n" +
                            "\n" +
                            "ИЗМЕНЕНИЯ В ПОВЕДЕНИИ:\n" +
                            dogReport.getBehavior()));
            telegramBot.execute(new SendMessage(volunteerChatId,
                    DOG_REPORT_EVALUATION_TEXT +
                            "/5_dog_" + dogReportId + RATING_EQUALS_FIVE_TEXT +
                            "/4_dog_" + dogReportId + RATING_EQUALS_FOUR_TEXT +
                            "/3_dog_" + dogReportId + RATING_EQUALS_THREE_TEXT +
                            "/2_dog_" + dogReportId + " и /1_dog_" + dogReportId + RATING_EQUALS_TWO_OR_ONE_TEXT
            ));
        } else telegramBot.execute(new SendMessage(volunteerChatId, DOG_REPORT_NOT_FOUND_BY_ID_TEXT
                + dogReportId + WAY_BACK_TEXT));

    }

    /**
     * Метод для выставления оценки по качеству заполнения отчета усыновителем
     *
     * @param volunteerChatId Идентификатор чата волонтера.
     * @param messageText     текст сообщения от волонтера.
     */
    protected void evaluateReport(Long volunteerChatId, String messageText) {
        messageText = messageText.replaceAll("/", "");
        String[] petReportEvaluation = messageText.split("_");
        if (petReportEvaluation.length != 3) {
            telegramBot.execute(new SendMessage(volunteerChatId, DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT));
            return;
        }
        try {
            Integer petReportRating = Integer.parseInt(petReportEvaluation[0].replaceAll("\\s+", ""));
            Long petReportId = Long.parseLong(petReportEvaluation[2].replaceAll("\\s+", ""));
            if (!checkValidRangeOfRating(volunteerChatId, petReportRating)) {
                return;
            }
            if (petReportEvaluation[1].equals("cat")) {
                evaluateCatReport(volunteerChatId, petReportId, petReportRating);
            } else if (petReportEvaluation[1].equals("dog")) {
                evaluateDogReport(volunteerChatId, petReportId, petReportRating);
            } else telegramBot.execute(new SendMessage(volunteerChatId,
                    "Такого животного я пока не знаю.\n" + WAY_BACK_TEXT));
        } catch (NumberFormatException e) {
            telegramBot.execute(new SendMessage(volunteerChatId, DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT));
        }
    }

    /**
     * Метод для проверки, что оценка волонтера не выходит за рамки пятибальной шкалы
     *
     * @param volunteerChatId Идентификатор чата волонтера.
     * @param petReportRating оценка волонтера.
     */
    private Boolean checkValidRangeOfRating(Long volunteerChatId, Integer petReportRating) {
        if (petReportRating > 0 && petReportRating < 6) {
            return true;
        } else {
            telegramBot.execute(new SendMessage(volunteerChatId,
                    "Ваша оценка выходит за рамки пятибальной шкалы" + WAY_BACK_TEXT));
            return false;
        }
    }

    /**
     * Метод для уведомления усыновителя кошки от том, что оценка отчета неудовлетворительная
     *
     * @param petReportId     Идентификатор чата отчетов о питомцах.
     * @param petReportRating Оценка волонтера.
     */
    private void sendCatReportWarningMessage(Long petReportId, Integer petReportRating) {
        if (petReportRating == 1 || petReportRating == 2) {
            Long chatId = catReportRepository.findById(petReportId).orElseThrow().getUser().getChat().getChatId();
            telegramBot.execute(new SendMessage(chatId, BAD_COMPLETION_CAT_REPORT_TEXT));

        }
    }

    /**
     * Метод для уведомления усыновителя собаки от том, что оценка отчета неудовлетворительная
     *
     * @param petReportId     Идентификатор чата отчетов о питомцах.
     * @param petReportRating Оценка волонтера.
     */
    private void sendDogReportWarningMessage(Long petReportId, Integer petReportRating) {
        if (petReportRating == 1 || petReportRating == 2) {
            Long chatId = dogReportRepository.findById(petReportId).orElseThrow().getUser().getChat().getChatId();
            telegramBot.execute(new SendMessage(chatId, BAD_COMPLETION_DOG_REPORT_TEXT));
        }
    }


    /**
     * Метод для выставления оценки заполнения отчета по кошке и записи среднего рейтинга оценки в репозиторий
     *
     * @param volunteerChatId Идентификатор чата волонтера.
     * @param catReportId     Идентификатор отчета по кошке.
     * @param catReportRating Оценка волонтера по качеству заполнения отчета.
     */
    protected void evaluateCatReport(Long volunteerChatId, Long catReportId, Integer catReportRating) {
        if (catReportRepository.findById(catReportId).isPresent()) {
            CatReport catReport = catReportRepository.findById(catReportId).get();
            if (catReport.getReportVerified()) {
                telegramBot.execute(new SendMessage(volunteerChatId, "Этот отчет уже был оценен." + WAY_BACK_TEXT));
                return;
            }
            sendCatReportWarningMessage(catReportId, catReportRating);
            Long userId = catReport.getUser().getId();
            if (volunteerInfoCatRepository.findByUserId(userId).isEmpty()) {
                telegramBot.execute(new SendMessage(volunteerChatId, "Не найдена информация в volunteerInfoCat. " +
                        "Невозможно проставить оценку."
                        + WAY_BACK_TEXT));
                return;
            }
            VolunteerInfoCat volunteerInfoCat = volunteerInfoCatRepository.findByUserId(userId).get();
            Double currentAverageRating = volunteerInfoCat.getRating();
            Integer currentAmountOfDays = volunteerInfoCat.getAmountOfDays();
            Double newAverageRating = ((currentAverageRating * (currentAmountOfDays + 1)) + catReportRating) / (currentAmountOfDays + 1);
            volunteerInfoCat.setRating(newAverageRating);
            volunteerInfoCatRepository.save(volunteerInfoCat);
            catReport.setReportVerified(true);
            catReportRepository.save(catReport);
            telegramBot.execute(new SendMessage(volunteerChatId, "Кошачий отчет с идентификатором "
                    + catReportId + " успешно оценен." +
                    " Зафиксирована оценка равная "
                    + catReportRating + "\n" +
                    "На " + currentAmountOfDays + " день усыновления текущая средняя оценка по качеству заполнению отчетов равна "
                    + newAverageRating + "\n" +
                    WAY_BACK_TEXT));
        } else telegramBot.execute(new SendMessage(volunteerChatId, CAT_REPORT_NOT_FOUND_BY_ID_TEXT
                + catReportId + WAY_BACK_TEXT));

    }

    /**
     * Метод для выставления оценки заполнения отчета по собаке и записи среднего рейтинга оценки в репозиторий
     *
     * @param volunteerChatId Идентификатор чата волонтера.
     * @param dogReportId     Идентификатор отчета по собаке.
     * @param dogReportRating Оценка волонтера по качеству заполнения отчета.
     */
    protected void evaluateDogReport(Long volunteerChatId, Long dogReportId, Integer dogReportRating) {
        if (dogReportRepository.findById(dogReportId).isPresent()) {
            DogReport dogReport = dogReportRepository.findById(dogReportId).get();
            if (dogReport.getReportVerified()) {
                telegramBot.execute(new SendMessage(volunteerChatId, "Этот отчет уже был оценен." + WAY_BACK_TEXT));
                return;
            }
            sendDogReportWarningMessage(dogReportId, dogReportRating);
            Long userId = dogReport.getUser().getId();
            if (volunteerInfoDogRepository.findByUserId(userId).isEmpty()) {
                telegramBot.execute(new SendMessage(volunteerChatId, "Не найдена информация в volunteerInfoDog. " +
                        "Невозможно проставить оценку."
                        + WAY_BACK_TEXT));
                return;
            }
            VolunteerInfoDog volunteerInfoDog = volunteerInfoDogRepository.findByUserId(userId).get();
            Double currentAverageRating = volunteerInfoDog.getRating();
            Integer currentAmountOfDays = volunteerInfoDog.getAmountOfDays();
            Double newAverageRating = ((currentAverageRating * (currentAmountOfDays + 1)) + dogReportRating) / (currentAmountOfDays + 1);
            volunteerInfoDog.setRating(newAverageRating);
            volunteerInfoDogRepository.save(volunteerInfoDog);
            dogReport.setReportVerified(true);
            dogReportRepository.save(dogReport);
            telegramBot.execute(new SendMessage(volunteerChatId, "Собачий отчет с идентификатором "
                    + dogReportId + " успешно оценен." +
                    " Зафиксирована оценка равная "
                    + dogReportRating + "\n" +
                    "На " + currentAmountOfDays + " день усыновления текущая средняя оценка по качеству заполнению отчетов равна "
                    + newAverageRating + "\n" +
                    WAY_BACK_TEXT));
        } else telegramBot.execute(new SendMessage(volunteerChatId, "Собачий отчет с идентификатором "
                + dogReportId + " отстутствует." + WAY_BACK_TEXT));

    }

    /**
     * Метод получения отчетов по кошкам
     *
     * @param volunteerChatId Идентификатор чата волонтера.
     */
    protected void getAllCatReports(Long volunteerChatId) {
        if (!catReportRepository.findAll().isEmpty()) {
            List<String> reportIdList = catReportRepository.findAll().stream()
                    .filter(catReport -> !catReport.getReportVerified())
                    .map(catReport -> "/cat_" + catReport.getId())
                    .collect(Collectors.toList());
            if (reportIdList.isEmpty()) {
                telegramBot.execute(new SendMessage(volunteerChatId, CAT_REPORT_EMPTY_LIST_TEXT));
                return;
            }
            telegramBot.execute(new SendMessage(volunteerChatId, CAT_REPORT_LIST_TEXT + reportIdList + "\n"));
        } else telegramBot.execute(new SendMessage(volunteerChatId, CAT_REPORT_EMPTY_LIST_TEXT));

    }

    /**
     * Метод получения отчетов по собакам
     *
     * @param volunteerChatId Идентификатор чата волонтера.
     */
    protected void getAllDogReports(Long volunteerChatId) {
        if (!dogReportRepository.findAll().isEmpty()) {
            List<String> reportIdList = dogReportRepository.findAll().stream()
                    .filter(dogReport -> !dogReport.getReportVerified())
                    .map(dogReport -> "/dog_" + dogReport.getId())
                    .collect(Collectors.toList());
            if (reportIdList.isEmpty()) {
                telegramBot.execute(new SendMessage(volunteerChatId, DOG_REPORT_EMPTY_LIST_TEXT + WAY_BACK_TEXT));
                return;
            }
            telegramBot.execute(new SendMessage(volunteerChatId, DOG_REPORT_LIST_TEXT + reportIdList + "\n"));
        } else telegramBot.execute(new SendMessage(volunteerChatId, DOG_REPORT_EMPTY_LIST_TEXT + WAY_BACK_TEXT));


    }
}
