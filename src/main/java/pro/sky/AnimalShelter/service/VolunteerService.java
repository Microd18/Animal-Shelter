package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.*;
import pro.sky.AnimalShelter.repository.*;
import pro.sky.AnimalShelter.utils.ValidationUtils;

import java.util.List;
import java.util.stream.Collectors;

import static pro.sky.AnimalShelter.utils.MessagesBot.*;

/**
 * Сервис для обработки команд меню волонтера.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VolunteerService {
    /**
     * Репозиторий для доступа к данным о юзере.
     */
    private final UserRepository userRepository;

    /**
     * Репозиторий для доступа к данным о собаках.
     */
    private final DogRepository dogRepository;

    /**
     * Репозиторий для доступа к данным о кошках.
     */
    private final CatRepository catRepository;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Утилиты для валидации данных.
     */
    private final ValidationUtils validationUtils;

    /**
     * Репозиторий для доступа к информации для волонтера о кошках.
     */
    private final VolunteerInfoDogRepository volunteerInfoDogRepository;
    /**
     * Репозиторий для доступа к информации для волонтера о собаках.
     */
    private final VolunteerInfoCatRepository volunteerInfoCatRepository;

    /**
     * Метод для поиска юзеров по номеру телефона.
     *
     * @param chatId идентификатор чата пользователя.
     * @param phone  номер телефона извлекаемый из сообщения юзера
     */
    public void findUsersByPhone(Long chatId, String phone) {
        List<User> userList = userRepository.findAll().stream()
                .filter(user ->
                        user.getPhone()
                                .equals(validationUtils.phoneNumberFormat(phone)))
                .collect(Collectors.toList());
        if (userList.isEmpty()) {
            telegramBot.execute(new SendMessage(chatId, USER_NOT_FOUND_BY_PHONE_TEXT + WAY_BACK_TEXT));
        } else {
            telegramBot.execute(new SendMessage(chatId, USER_FOUND_BY_PHONE_TEXT + userList + WAY_BACK_TEXT));
        }
    }

    /**
     * Метод для поиска питомцев по кличке.
     *
     * @param chatId      идентификатор чата пользователя.
     * @param messageText текст сообщения, содержащего данные о животном в формате: Кошка/Собака, Кличка.
     */
    public void findAnimalByName(Long chatId, String messageText) {
        String[] animalData = messageText.split(",");
        if (animalData.length != 2) {
            telegramBot.execute(new SendMessage(chatId, DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT));
            return;
        }
        String pet = animalData[0].replaceAll("\\s+", "");
        String nickname = animalData[1].replaceAll("\\s+", "");
        if (pet.equalsIgnoreCase("Кошка")) {
            List<Cat> catList = getCatsByName(nickname);
            if (catList.isEmpty()) {
                telegramBot.execute(new SendMessage(chatId, CAT_NOT_FOUND_BY_NAME_TEXT + nickname + WAY_BACK_TEXT));
            } else {
                telegramBot.execute(new SendMessage(chatId, ANIMAL_FOUND_BY_NAME_TEXT + catList + WAY_BACK_TEXT));
            }
        } else if (pet.equalsIgnoreCase("Собака")) {
            List<Dog> dogList = getDogsByName(nickname);
            if (dogList.isEmpty()) {
                telegramBot.execute(new SendMessage(chatId, DOG_NOT_FOUND_TEXT + nickname + WAY_BACK_TEXT));
            } else {
                telegramBot.execute(new SendMessage(chatId, ANIMAL_FOUND_BY_NAME_TEXT + dogList + WAY_BACK_TEXT));
            }
        } else telegramBot.execute(new SendMessage(chatId, DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT));

    }

    /**
     * Метод для перевода юзера в усыновителя.
     *
     * @param chatId      идентификатор чата пользователя.
     * @param messageText текст сообщения, содержащего данных об усыновителе и питомце в формате: user_id,Кошка/Собака, pet_id.
     */
    public void allowUserBecomeAdopter(Long chatId, String messageText) {
        String[] userAndPetData = messageText.split(",");
        if (userAndPetData.length != 3) {
            telegramBot.execute(new SendMessage(chatId, DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT));
            return;
        }
        try {
            Long userId = Long.parseLong(userAndPetData[0].replaceAll("\\s+", ""));
            String dogOrCat = userAndPetData[1].replaceAll("\\s+", "");
            Long petId = Long.parseLong(userAndPetData[2].replaceAll("\\s+", ""));
            if (dogOrCat.equalsIgnoreCase("Кошка")) {
                updateCatData(chatId, userId, petId);
            } else if (dogOrCat.equalsIgnoreCase("Собака")) {
                updateDogData(chatId, userId, petId);
            } else telegramBot.execute(new SendMessage(chatId, DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT));
        } catch (NumberFormatException e) {
            telegramBot.execute(new SendMessage(chatId, DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT));
        }
    }

    /**
     * Метод для сохранения user_id к кошке в таблице кошек.
     *
     * @param chatId идентификатор чата пользователя.
     * @param userId user_id пользователя.
     * @param catId  id кошки в таблице кошек.
     */
    protected void updateCatData(Long chatId, Long userId, Long catId) {
        catRepository.findById(catId)
                .ifPresentOrElse(
                        (foundCat -> userRepository.findById(userId)
                                .ifPresentOrElse(
                                        user -> {
                                            if (checkOneCatToOneAdopterCondition(chatId, foundCat, user)) {
                                                foundCat.setUser(user);
                                                catRepository.save(foundCat);
                                                saveUserOnCatReport(userId);
                                                telegramBot.execute(new SendMessage(chatId, ADOPTION_SUCCESS_TEXT));
                                            }
                                        },
                                        () -> telegramBot.execute(new SendMessage(chatId, USER_NOT_FOUND_BY_ID_TEXT + WAY_BACK_TEXT))
                                )
                        ), () -> telegramBot.execute(new SendMessage(chatId, CAT_NOT_FOUND_BY_ID_TEXT + WAY_BACK_TEXT)));
    }

    /**
     * Метод проверки, что у переданной кошки нет усыновителя, а переданный усыновитель ещё не взял кошку.
     *
     * @param chatId     идентификатор чата пользователя.
     * @param cat        кошка.
     * @param newAdopter юзер.
     */
    protected Boolean checkOneCatToOneAdopterCondition(Long chatId, Cat cat, User newAdopter) {
        if (cat.getUser() != null) {
            telegramBot.execute(new SendMessage(chatId, CAT_ALREADY_HAS_ADOPTER_TEXT + WAY_BACK_TEXT));
            return false;
        }
        if (newAdopter.getCat() != null) {
            telegramBot.execute(new SendMessage(chatId, ADOPTER_ALREADY_TOOK_CAT_TEXT + WAY_BACK_TEXT));
            return false;
        }
        return true;
    }

    /**
     * Метод для сохранения user_id к собаке в таблице собак.
     *
     * @param chatId идентификатор чата пользователя.
     * @param userId user_id пользователя.
     * @param dogId  id собаки в таблице собак.
     */
    protected void updateDogData(Long chatId, Long userId, Long dogId) {
        dogRepository.findById(dogId)
                .ifPresentOrElse(
                        (foundDog -> userRepository.findById(userId)
                                .ifPresentOrElse(
                                        user -> {
                                            if (checkOneDogToOneAdopterCondition(chatId, foundDog, user)) {
                                                foundDog.setUser(user);
                                                dogRepository.save(foundDog);
                                                saveUserOnDogReport(userId);
                                                telegramBot.execute(new SendMessage(chatId, ADOPTION_SUCCESS_TEXT));
                                            }
                                        },
                                        () -> telegramBot.execute(new SendMessage(chatId, USER_NOT_FOUND_BY_ID_TEXT + WAY_BACK_TEXT))
                                )
                        ), () -> telegramBot.execute(new SendMessage(chatId, DOG_NOT_FOUND_BY_ID_TEXT + WAY_BACK_TEXT)));
    }

    /**
     * Метод проверки, что у переданной собаки нет усыновителя, а переданный усыновитель ещё не взял собаку.
     *
     * @param chatId     идентификатор чата пользователя.
     * @param dog        собака.
     * @param newAdopter юзер.
     */
    protected Boolean checkOneDogToOneAdopterCondition(Long chatId, Dog dog, User newAdopter) {
        if (dog.getUser() != null) {
            telegramBot.execute(new SendMessage(chatId, DOG_ALREADY_HAS_ADOPTER_TEXT + WAY_BACK_TEXT));
            return false;
        }
        if (newAdopter.getDog() != null) {
            telegramBot.execute(new SendMessage(chatId, ADOPTER_ALREADY_TOOK_DOG_TEXT + WAY_BACK_TEXT));
            return false;
        }
        return true;
    }

    /**
     * Метод получения списка кошек по кличке.
     *
     * @param name кличка кошки.
     */
    private List<Cat> getCatsByName(String name) {
        return catRepository.findAll().stream()
                .filter(cat ->
                        cat.getNickname()
                                .equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Метод получения списка собак по кличке.
     *
     * @param name кличка собаки.
     */
    private List<Dog> getDogsByName(String name) {
        return dogRepository.findAll().stream()
                .filter(cat ->
                        cat.getNickname()
                                .equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    protected void saveUserOnCatReport(Long userId) {
        volunteerInfoCatRepository.save(new VolunteerInfoCat(0, 0D, userRepository.findById(userId).get()));
    }

    protected void saveUserOnDogReport(Long userId) {
        volunteerInfoDogRepository.save(new VolunteerInfoDog(0, 0D, userRepository.findById(userId).get()));
    }

}
