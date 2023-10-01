package pro.sky.AnimalShelter.handlers.volunteerMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;

import static pro.sky.AnimalShelter.enums.BotCommand.ADMIN;
import static pro.sky.AnimalShelter.enums.BotCommand.FIND_USER_BY_PHONE;
import static pro.sky.AnimalShelter.utils.MessagesBot.WAITING_PHONE_NUMBER_TEXT;

/**
 * Обработчик команды "/find_user_by_phone".
 */
@Service
@RequiredArgsConstructor
public class FindUserByPhoneCommandHandler implements CommandHandler {

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Обрабатывает команду "/find_user_by_phone" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == ADMIN) {
            SendMessage message = new SendMessage(chatId, WAITING_PHONE_NUMBER_TEXT);
            telegramBot.execute(message);
            chatStateService.updateChatState(chatId, FIND_USER_BY_PHONE);
        } else {
            telegramBot.execute(new SendMessage(chatId, "Сперва зайдите в меню волонтёра"));
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/find_user_by_phone").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return FIND_USER_BY_PHONE;
    }
}
