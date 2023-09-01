package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.enums.BotState;
import pro.sky.AnimalShelter.state.ChatStateHolder;

@Service
@RequiredArgsConstructor
public class CatCommandHandler implements CommandHandler {

    private final ChatStateHolder chatStateHolder;
    private final TelegramBot telegramBot;
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();

        if (chatStateHolder.getState(chatId) == BotState.START) {
            String responseText = "Вы выбрали приют для кошек. \n"
            + "Для уточнения информации по режиму работы приюта. Введите команду /schedule";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        } else {
            String responseText = "Для использования бота введите команду /start";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        }
    }

    @Override
    public BotCommand getCommand() {
        return BotCommand.CAT;
    }
}
