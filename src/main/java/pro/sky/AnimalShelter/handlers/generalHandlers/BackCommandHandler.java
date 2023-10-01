package pro.sky.AnimalShelter.handlers.generalHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.service.UserReportStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.*;

/**
 * Обработчик команды "/back". Позволяет вернуться в предыдущее меню.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BackCommandHandler implements CommandHandler {

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Сервис для управления очередью состояний отчётов пользователей.
     */
    private final UserReportStateService userReportStateService;

    /**
     * Экземпляр утилитарного класс для общих методов.
     */
    private final CommonUtils commonUtils;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Обрабатывает команду "/back" и возвращает пользователя в предыдущее меню.
     *
     * @param update Объект, содержащий информацию о сообщении пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        BotCommand lastState = chatStateService.getLastStateCatOrDogByChatId(chatId);
        if (currentState == SEND_REPORT) {
            telegramBot.execute(new SendMessage(chatId.toString(), BACK_COMMAND_SEND_REPORT));
            userReportStateService.clearUserReportStates(chatId);
            chatStateService.goToPreviousState(chatId);
        }
        if (currentState == CONTACT) {
            String shelterType = lastState == DOG ? "приюте для собак" : "приюте для кошек";
            String responseText = "Вы вернулись назад. Какую информацию вы бы хотели получить о " + shelterType + ":\n" +
                    SHELTER_INFO_COMMAND_TEXT;

            telegramBot.execute(new SendMessage(chatId.toString(), responseText));
            chatStateService.goToPreviousState(chatId);
        }

        if (currentState == FIND_USER_BY_PHONE || currentState == FIND_ANIMAL_BY_NAME
                || currentState == MAKE_ADOPTER || currentState == CHECK_REPORT || currentState == EXTENSION_PROBATION
        || currentState == SUCCESSFUL_PROBATIONARY) {
            SendMessage message = new SendMessage(chatId.toString(), ADMIN_COMMAND_RETURN_TEXT);
            telegramBot.execute(message);
            chatStateService.goToPreviousState(chatId);
        }

        if (currentState == SHELTER_INFO || currentState == ADOPT || currentState == PET_REPORT) {

            BotCommand previousState = chatStateService.getLastStateCatOrDogByChatId(chatId);

            var shelterType = previousState == CAT ? "приют для кошек" : "приют для собак";
            String responseText = "Вы вернулись назад. У вас выбран " + shelterType + SHELTER_COMMAND_TEXT;

            telegramBot.execute(new SendMessage(chatId.toString(), responseText));
            chatStateService.goToPreviousState(chatId);
        } else if (currentState == DOG || currentState == CAT || currentState == START) {

            telegramBot.execute(new SendMessage(chatId.toString(), BACK_COMMAND_GENERAL_MENU_TEXT));
            if (currentState != START) {
                chatStateService.goToPreviousState(chatId);
            }
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        }
    }

    /**
     * Получает команду, обрабатываемую этим обработчиком.
     *
     * @return Команда обработчика.
     */
    @Override
    public BotCommand getCommand() {
        return BACK;
    }
}
