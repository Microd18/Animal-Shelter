package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.state.ChatStateHolder;

import static pro.sky.AnimalShelter.enums.BotCommand.STOP;

@Slf4j
@Service
@RequiredArgsConstructor
public class StopCommandHandler implements CommandHandler {

    private final TelegramBot telegramBot;
    private final ChatStateHolder chatStateHolder;
    @Override
    public void handle(Update update) {
        log.info("Bot received /stop command. Shutting down...");
        Long chatId = update.message().chat().id();
        telegramBot.execute(new SendMessage(chatId.toString(), "Бот выключен. Для включения бота отправьте команду /start."));
        chatStateHolder.addState(chatId, STOP);
        chatStateHolder.setBotStarted(chatId, false);
    }

    @Override
    public BotCommand getCommand() {
        return STOP;
    }
}
