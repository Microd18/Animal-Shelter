package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.state.ChatStateHolder;

import static pro.sky.AnimalShelter.enums.BotCommand.PASS;
import static pro.sky.AnimalShelter.enums.BotCommand.SHELTER_INFO;

/**
 * Обработчик команды "/pass".
 */
@Service
@RequiredArgsConstructor
public class PassCommandHandler implements CommandHandler {

    /**
     * Хранилище состояний чатов.
     */
    private final ChatStateHolder chatStateHolder;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Обрабатывает команду "/pass" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateHolder.getCurrentStateById(chatId);

        if (currentState == SHELTER_INFO) {
            String responseText = "Пункт охраны находится по адресу:\n" +
                    "ул. Аккорган, 5/3, микрорайон Коктал, Астана\n" +
                    "телефон для связи: +7(999)4567890\n\n" +
                    "Для получения пропуска при себе иметь: \n" +
                    "-Удостоверение личности \n" +
                    "-Документы на автомобиль \n\n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        } else {
            String responseText = "Для использования бота введите команду /start";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        }

    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/pass").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return PASS;
    }
}
