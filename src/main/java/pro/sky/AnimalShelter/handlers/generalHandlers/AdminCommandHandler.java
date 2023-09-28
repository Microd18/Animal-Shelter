package pro.sky.AnimalShelter.handlers.generalHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatService;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static pro.sky.AnimalShelter.enums.BotCommand.ADMIN;
import static pro.sky.AnimalShelter.utils.MessagesBot.ADMIN_COMMAND_TEXT;

@Service
@RequiredArgsConstructor
public class AdminCommandHandler implements CommandHandler {

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    private final ChatService chatService;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Экземпляр утилитарного класс для общих методов.
     */
    private final CommonUtils commonUtils;

    /**
     * Обрабатывает команду "/admin" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();

        if (chatService.isBotStarted(chatId)) {
            telegramBot.execute(new SendMessage(chatId.toString(), ADMIN_COMMAND_TEXT));
            chatStateService.updateChatState(chatId, ADMIN);
        } else {
            commonUtils.offerToStart(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/admin").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return ADMIN;
    }

}
