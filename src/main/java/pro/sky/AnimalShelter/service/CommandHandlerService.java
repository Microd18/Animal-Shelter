package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.handlers.CommandHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandHandlerService {

    private final TelegramBot telegramBot;
    private final List<CommandHandler> commandHandlers;


    public void process(Update update) {
        Message message = update.message();
        if (message != null && message.text() != null) {
            String messageText = message.text();

            List<CommandHandler> matchedHandlers = commandHandlers.stream()
                    .filter(commandHandler -> commandHandler.getCommand().getCommandText().equals(messageText))
                    .collect(Collectors.toList());

            if (!matchedHandlers.isEmpty()) {
                matchedHandlers.get(0).handle(update);
                return;
            }
        }
        handleUnknownCommand(update);
    }

    private void handleUnknownCommand(Update update) {
        Message message = update.message();
        telegramBot.execute(new SendMessage(message.chat().id(), "Неизвестная команда"));
    }
}
