package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.Cat;
import pro.sky.AnimalShelter.entity.Dog;
import pro.sky.AnimalShelter.entity.User;
import pro.sky.AnimalShelter.repository.CatRepository;
import pro.sky.AnimalShelter.repository.DogRepository;
import pro.sky.AnimalShelter.repository.UserRepository;
import pro.sky.AnimalShelter.utils.ValidationUtils;

import java.util.List;
import java.util.stream.Collectors;

import static pro.sky.AnimalShelter.utils.MessagesBot.*;

/**
 * Сервис для обработки команд меню волонтера.
 */
@Slf4j
@Service
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
     * Метод для вывода списка айди всех усыновителей.
     *
     * @param chatId идентификатор чата пользователя.
     */
    public void getAllAdopters(Long chatId) {
        List<Long> catAdopterIdSet = catRepository.getCatAdopters();
        List<Long> dogAdopterIdSet = dogRepository.getDogAdopters();
        if (catAdopterIdSet.isEmpty() && dogAdopterIdSet.isEmpty()) {
            telegramBot.execute(new SendMessage(chatId, ADOPTERS_NOT_FOUND_TEXT + WAY_BACK_TEXT));
        } else if (!catAdopterIdSet.isEmpty() && !dogAdopterIdSet.isEmpty()) {
            telegramBot.execute(
                    new SendMessage(chatId,
                            CAT_ADOPTERS_FOUND_TEXT + catAdopterIdSet + "\n"
                                    + DOG_ADOPTERS_FOUND_TEXT + dogAdopterIdSet + "\n"
                                    + WAY_BACK_TEXT)
            );

        } else if (!catAdopterIdSet.isEmpty()) {
            telegramBot.execute(
                    new SendMessage(chatId,
                            CAT_ADOPTERS_FOUND_TEXT + catAdopterIdSet + "\n"
                                    + DOG_ADOPTERS_NOT_FOUND_TEXT
                                    + WAY_BACK_TEXT)
            );
        } else {
            telegramBot.execute(
                    new SendMessage(chatId,
                            DOG_ADOPTERS_FOUND_TEXT + dogAdopterIdSet + "\n"
                                    + CAT_ADOPTERS_NOT_FOUND_TEXT
                                    + WAY_BACK_TEXT)
            );
        }
    }


    /**
     * Метод для поиска питомцев по кличке.
     *
     * @param chatId      идентификатор чата пользователя.
     * @param messageText текст сообщения, содержащего данных о животном в формате: Кошка/Собака, Кличка.
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


    @Transactional
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
            telegramBot.execute(new SendMessage(chatId, ADOPTION_SUCCESS_TEXT));
        } catch (NumberFormatException e) {
            telegramBot.execute(new SendMessage(chatId, DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT));
        }
    }

    protected void updateCatData(Long chatId, Long userId, Long catId) {
        catRepository.findById(catId)
                .ifPresentOrElse(
                        (foundCat -> userRepository.findById(userId)
                                .ifPresentOrElse(
                                        user -> {
                                            foundCat.setUser(user);
                                            catRepository.save(foundCat);
                                        },
                                        () -> telegramBot.execute(new SendMessage(chatId, USER_NOT_FOUND_BY_ID_TEXT))
                                )
                        ), () -> telegramBot.execute(new SendMessage(chatId, CAT_NOT_FOUND_BY_ID_TEXT)));
    }

    protected void updateDogData(Long chatId, Long userId, Long dogId) {
        dogRepository.findById(dogId)
                .ifPresentOrElse(
                        (foundDog -> userRepository.findById(userId)
                                .ifPresentOrElse(
                                        user -> {
                                            foundDog.setUser(user);
                                            dogRepository.save(foundDog);
                                        },
                                        () -> telegramBot.execute(new SendMessage(chatId, USER_NOT_FOUND_BY_ID_TEXT))
                                )
                        ), () -> telegramBot.execute(new SendMessage(chatId, DOG_NOT_FOUND_BY_ID_TEXT)));
    }


    private List<Cat> getCatsByName(String name) {
        return catRepository.findAll().stream()
                .filter(cat ->
                        cat.getNickname()
                                .equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    private List<Dog> getDogsByName(String name) {
        return dogRepository.findAll().stream()
                .filter(cat ->
                        cat.getNickname()
                                .equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

}
