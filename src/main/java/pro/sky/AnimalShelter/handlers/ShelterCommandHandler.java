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
public abstract class ShelterCommandHandler implements CommandHandler {

    private final ChatStateHolder chatStateHolder;
    private final TelegramBot telegramBot;
    private final BotCommand selectedCommand;
    private final String shelterType;

    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateHolder.getCurrentStateById(chatId);

        if (currentState == BotCommand.START) {
            String responseText = "Вы выбрали " + shelterType + ". Чем я могу помочь?\n" +
                    "1. Узнать информацию о приюте (/shelter_info)\n" +
                    "2. Как взять животное из приюта (/adopt)\n" +
                    "3. Прислать отчет о питомце (/pet_report)\n" +
                    "4. Позвать волонтера (/help)\n" +
                    "5. Назад (/back)\n" +
                    "6. Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
            chatStateHolder.addState(chatId, selectedCommand);
        } else if (currentState == CAT || currentState == DOG) {
            var shelter = currentState == CAT ? "приют для кошек" : "приют для собак";
            String responseText = "Вы уже выбрали " + shelter + ".\n" +
                    " Для возврата в предыдущее меню введите команду назад /back,\n" +
                    " Чтобы выключить бота введите команду /stop";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        } else if (currentState == STOP) {
            String responseText = "Для использования бота введите команду /start";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        } else {
            String responseText = "Данная команда не допустима вэтом меню.\n" +
                    " Для возврата в предыдущее меню введите команду назад /back,\n" +
                    " Чтобы выключить бота введите команду /stop";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        }
    }
}
