package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.state.ChatStateHolder;

import static pro.sky.AnimalShelter.enums.BotCommand.*;

/**
 * Абстрактный класс для обработки команд в контексте приютов.
 */
@Service
@RequiredArgsConstructor
public abstract class ShelterCommandHandler implements CommandHandler {

    /**
     * Хранилище состояний чатов.
     */
    private final ChatStateHolder chatStateHolder;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Команда, связанная с текущим обработчиком.
     */
    private final BotCommand selectedCommand;

    /**
     * Тип приюта, связанный с текущим обработчиком (например, "приют для кошек").
     */
    private final String shelterType;

    /**
     * Обрабатывает команду, основываясь на текущем состоянии чата и выбранной команде.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateHolder.getCurrentStateById(chatId);

        if (currentState == START) {
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
