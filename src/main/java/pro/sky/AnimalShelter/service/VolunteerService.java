package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
     * Получает список юзеров по номеру телефона
     *
     * @param phone Номер телефона
     * @return Список юзеров, найденных по номеру телефона. Пустой, если юзер не найден
     */
    public String findUsersByPhone(String phone) {
        List<User> userList = userRepository.findAll().stream()
                .filter(user ->
                        user.getPhone()
                                .equals(validationUtils.phoneNumberFormat(phone)))
                .collect(Collectors.toList());
        if (userList.isEmpty()) {
            return USER_NOT_FOUND_TEXT + WAY_BACK_TEXT;
        } else {
            return USER_FOUND_BY_PHONE_TEXT + userList + WAY_BACK_TEXT;
        }
    }

    public String findAnimalByName(String messageText) {
        String[] animalData = messageText.split(",");
        if (animalData.length != 2) {
            return DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT;
        } else if (animalData[0].equalsIgnoreCase("Кошка")) {
            List<Cat> catList = getCatsByName(animalData[1]);
            if (catList.isEmpty()) {
                return CAT_NOT_FOUND_TEXT + animalData[1] + WAY_BACK_TEXT;
            } else {
                return ANIMAL_FOUND_BY_NAME_TEXT + catList + WAY_BACK_TEXT;
            }
        } else if (animalData[0].equalsIgnoreCase("Собака")) {
            List<Dog> dogList = getDogsByName(animalData[1]);
            if (dogList.isEmpty()) {
                return DOG_NOT_FOUND_TEXT + animalData[1] + WAY_BACK_TEXT;
            } else {
                return ANIMAL_FOUND_BY_NAME_TEXT + dogList + WAY_BACK_TEXT;
            }
        } else return DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT;
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
