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
 * Обработчик команды "/shelter_info".
 */
@Service
@RequiredArgsConstructor
public class ShelterInfoCommandHandler implements CommandHandler {

    /**
     * Хранилище состояний чатов.
     */
    private final ChatStateHolder chatStateHolder;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Обрабатывает команду "/shelter_info" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateHolder.getCurrentStateById(chatId);

        if (currentState == DOG || currentState == CAT || currentState == SHELTER_INFO) {
            BotCommand previousState = chatStateHolder.getPreviousState(chatId);
            String s = currentState == SHELTER_INFO ? "Вы уже в этом меню." : "";
            String shelterType = currentState == DOG ? "приюте для собак" : currentState == SHELTER_INFO
                    ? previousState == DOG ? "приюте для собак" : "приюте для кошек" : "приюте для кошек";
            String responseText = s + "Какую информацию вы бы хотели получить о " + shelterType + ":\n" +
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

            if (!(currentState == SHELTER_INFO)) {
                chatStateHolder.addState(chatId, SHELTER_INFO);
            }
        } else if (currentState == STOP) {
            String responseText = "Для использования бота введите команду /start";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/shelter_info").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return SHELTER_INFO;
    }
}
