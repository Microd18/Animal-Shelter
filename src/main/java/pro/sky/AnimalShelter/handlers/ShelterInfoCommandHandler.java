package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.state.ChatStateHolder;

import static pro.sky.AnimalShelter.enums.BotCommand.*;

@Service
@RequiredArgsConstructor
public class ShelterInfoCommandHandler implements CommandHandler {

    private final ChatStateHolder chatStateHolder;
    private final TelegramBot telegramBot;

    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateHolder.getState(chatId);

        if (currentState == DOG || currentState == CAT) {
            chatStateHolder.pushState(chatId, currentState);
            String shelterType = currentState == DOG ? "приюте для собак" : "приюте для кошек";
            String responseText = "Какую информацию вы бы хотели получить о " + shelterType + ":\n" +
                    "1. Описание приюта (/description)\n" +
                    "2. Расписание работы и контакты (/schedule)\n" +
                    "3. Контактные данные охраны для пропуска (/pass)\n" +
                    "4. Техника безопасности на территории приюта (/safety)\n" +
                    "5. Оставить контактные данные (/contact)\n" +
                    "6. Позвать волонтера (/help)\n" +
                    "7. Назад (/back)\n" +
                    "8. Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);

            chatStateHolder.setState(chatId, BotCommand.SHELTER_INFO);
        } else if (currentState == STOP) {
            String responseText = "Для использования бота введите команду /start";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        }
    }

    @Override
    public BotCommand getCommand() {
        return BotCommand.SHELTER_INFO;
    }
}
