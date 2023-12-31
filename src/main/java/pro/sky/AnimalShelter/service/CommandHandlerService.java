package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.handlers.CommandHandler;

import java.util.List;
import java.util.stream.Collectors;

import static pro.sky.AnimalShelter.enums.BotCommand.CONTACT;

/**
 * Сервис для обработки команд.
 */
@Service
@RequiredArgsConstructor
public class CommandHandlerService {

    private final UserService userService;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Список обработчиков команд.
     */
    private final List<CommandHandler> commandHandlers;

    /**
     * Обработка входящего обновления.
     *
     * @param update Обновление от Telegram.
     */
    public void process(Update update) {
        Message message = update.message();
        if (message != null) {
            if (message.text() != null) {
                String messageText = message.text();

                List<CommandHandler> matchedHandlers = commandHandlers.stream()
                        .filter(commandHandler -> commandHandler.getCommand().getCommandText().equals(messageText))
                        .collect(Collectors.toList());

                if (!matchedHandlers.isEmpty()) {
                    matchedHandlers.get(0).handle(update);
                    return;
                }
            }
            if (message.chat() != null) {
                Long chatId = message.chat().id();
                if (chatStateService.getCurrentStateByChatId(chatId) == CONTACT) {
                    userService.updateContact(chatId, message.text());
                    return;
                }
            }
        }
        handleUnknownCommand(update);
    }

    /**
     * Обработка неизвестной команды.
     *
     * @param update Обновление от Telegram.
     */
    private void handleUnknownCommand(Update update) {
        Message message = update.message();
//        telegramBot.execute(new SendMessage(message.chat().id(), "Неизвестная команда"));
    }
}
