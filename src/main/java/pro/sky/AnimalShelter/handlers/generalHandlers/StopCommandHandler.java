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

import static pro.sky.AnimalShelter.enums.BotCommand.STOP;

/**
 * Обработчик команды /stop.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StopCommandHandler implements CommandHandler {

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Обрабатывает команду /stop и выключает бота.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        log.info("Bot received /stop command. Shutting down...");
        Long chatId = update.message().chat().id();
        telegramBot.execute(new SendMessage(chatId.toString(), "Бот выключен. Для включения бота отправьте команду /start."));
        chatStateService.stopBot(chatId);
    }

    /**
     * Возвращает команду, которую обрабатывает данный обработчик (/stop).
     *
     * @return Команда /stop.
     */
    @Override
    public BotCommand getCommand() {
        return STOP;
    }
}
