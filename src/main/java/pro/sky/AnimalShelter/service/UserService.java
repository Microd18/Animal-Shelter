package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.entity.User;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.UserRepository;
import pro.sky.AnimalShelter.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Сервис для обновления контактных данных пользователей.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    /**
     * Репозиторий для доступа к данным о юзере.
     */
    private final UserRepository userRepository;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Репозиторий для доступа к данным о чатах.
     */
    private final ChatRepository chatRepository;

    /**
     * Утилиты для валидации данных.
     */
    private final ValidationUtils validationUtils;

    /**
     * Метод для обновления контактных данных пользователя.
     *
     * @param chatId      идентификатор чата пользователя.
     * @param messageText текст сообщения, содержащего новые контактные данные в формате: Имя, Телефон, Email.
     */
    public void updateContact(Long chatId, String messageText) {
        log.info("updateContact method was invoked");

        String[] contactData = messageText.split(",");

        if (contactData.length != 3) {
            sendInvalidInputMessage(chatId);
            return;
        }

        var invalidFields = IntStream.range(0, contactData.length)
                .mapToObj(index -> {
                    var data = contactData[index].trim();
                    return CompletableFuture.supplyAsync(() -> isValidContactData(data, index))
                            .thenApply(valid -> valid ? null : index)
                            .join();
                })
                .filter(Objects::nonNull)
                .map(fieldIndex -> {
                    switch (fieldIndex) {
                        case 0:
                            return "Имя";
                        case 1:
                            return "Телефон";
                        case 2:
                            return "Email";
                        default:
                            return "Поле " + fieldIndex;
                    }
                })
                .collect(Collectors.toList());

        if (invalidFields.isEmpty()) {
            updateUserData(chatId, contactData[0].trim(), contactData[1].trim(), contactData[2].trim());
        } else {
            sendValidationErrors(chatId, invalidFields);
        }
    }

    /**
     * Метод для отправки сообщений об ошибках валидации.
     *
     * @param chatId        идентификатор чата пользователя.
     * @param invalidFields список невалидных полей.
     */
    protected void sendValidationErrors(Long chatId, List<String> invalidFields) {
        log.info("sendValidationErrors method was invoked");
        List<String> errorMessages = new ArrayList<>();
        if (invalidFields.contains("Имя")) {
            errorMessages.add("Имя может содержать только буквы и пробелы.");
        }
        if (invalidFields.contains("Телефон")) {
            errorMessages.add("Введен неверный номер телефона.");
        }
        if (invalidFields.contains("Email")) {
            errorMessages.add("Введен неверный Email.");
        }
        errorMessages.add("Возврат в предыдущее меню (/back)\n" +
                "Выключить бота (/stop)");
        telegramBot.execute(new SendMessage(chatId, String.join("\n", errorMessages)));
    }

    /**
     * Метод для обновления данных пользователя в базе данных.
     *
     * @param chatId идентификатор чата пользователя.
     * @param name   имя пользователя.
     * @param phone  номер телефона пользователя.
     * @param email  адрес электронной почты пользователя.
     */
    protected void updateUserData(Long chatId, String name, String phone, String email) {
        log.info("updateUserData method was invoked");
        userRepository.findByChatId(chatRepository.findByChatId(chatId).map(Chat::getId).orElse(null)).ifPresentOrElse(
                user -> {
                    user.setUsername(StringUtils.capitalize(name));
                    user.setPhone(validationUtils.phoneNumberFormat(phone));
                    user.setEmail(email);
                    userRepository.save(user);
                },
                () -> {
                    User user = new User();
                    user.setUsername(StringUtils.capitalize(name));
                    user.setPhone(validationUtils.phoneNumberFormat(phone));
                    user.setEmail(email);
                    chatRepository.findByChatId(chatId).ifPresent(user::setChat);
                    userRepository.save(user);
                }
        );
        telegramBot.execute(new SendMessage(chatId,
                "Контактные данные успешно обновлены.\n" +
                "Возврат в предыдущее меню (/back)\n" +
                "Выключить бота (/stop)"));
    }

    /**
     * Метод для отправки сообщения об неверном формате ввода.
     *
     * @param chatId идентификатор чата пользователя.
     */
    protected void sendInvalidInputMessage(Long chatId) {
        log.info("sendInvalidInputMessage method was invoked");
        SendMessage message = new SendMessage(chatId, "Для занесения или перезаписи, пожалуйста, " +
                "введите контактные данные в формате: Имя, Телефон, Email (даже если хотите поменять не все данные)\n" +
                "Возврат в предыдущее меню (/back)\n" +
                "Выключить бота (/stop)");
        telegramBot.execute(message);
    }

    /**
     * Метод для валидации контактных данных по индексу поля.
     *
     * @param data  данные для валидации.
     * @param index индекс поля данных (0 - Имя, 1 - Телефон, 2 - Email).
     * @return true, если данные действительны, и false в противном случае.
     */
    protected boolean isValidContactData(String data, int index) {
        log.info("isValidContactData method was invoked");
        switch (index) {
            case 0:
                return validationUtils.isValidName(data);
            case 1:
                return validationUtils.isValidPhoneNumber(data);
            case 2:
                return validationUtils.isValidEmail(data);
            default:
                return false;
        }
    }
}
