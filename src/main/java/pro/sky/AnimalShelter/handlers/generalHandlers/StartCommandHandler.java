package pro.sky.AnimalShelter.handlers.generalHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatService;
import pro.sky.AnimalShelter.service.ChatStateService;

import static pro.sky.AnimalShelter.enums.BotCommand.START;
import static pro.sky.AnimalShelter.utils.MessagesBot.START_TEXT;

/**
 * Обработчик команды "/start".
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {

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
     * Обрабатывает команду "/start" и инициализирует бота.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        log.info("Bot received the /start command. Inclusion...");
        Long chatId = update.message().chat().id();
        if (chatService.isBotStarted(chatId)) {
            telegramBot.execute(new SendMessage(chatId.toString(), "Бот уже запущен"));
            return;
        }
        telegramBot.execute(new SendMessage(chatId.toString(), START_TEXT));
        chatStateService.updateChatState(chatId, START);
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/start").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return START;
    }
}
