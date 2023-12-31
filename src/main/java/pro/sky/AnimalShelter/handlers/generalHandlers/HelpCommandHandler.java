package pro.sky.AnimalShelter.handlers.generalHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static pro.sky.AnimalShelter.enums.BotCommand.*;

/**
 * Обработчик команды "/help".
 */
@Service
@RequiredArgsConstructor
public class HelpCommandHandler implements CommandHandler {

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Экземпляр утилитарного класс для общих методов.
     */
    private final CommonUtils commonUtils;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Обрабатывает команду "/help" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == DOG || currentState == CAT || currentState == SHELTER_INFO || currentState == ADOPT ) {
            String shelterType = currentState == DOG ? "приюте для собак" : "приюте для кошек";
            String responseText = "Для связи с волонтером пройдите по ссылке: \n" +
                    "\n" +
                    "По четным дням месяца Вам поможет Дмитрий, ссылка на Телеграмм - https://t.me/DmitriyVolkov \n" +
                    "\n" +
                    "По нечетным дням месяца Вам поможет Елена, ссылка на Телеграмм - https://t.me/koroliana \n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/help").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return HELP;
    }
}