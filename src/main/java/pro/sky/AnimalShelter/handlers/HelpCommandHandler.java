package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.state.ChatStateHolder;

import static pro.sky.AnimalShelter.enums.BotCommand.*;

/**
 * Обработчик команды "/help".
 */
@Service
@RequiredArgsConstructor
public class HelpCommandHandler implements CommandHandler {

    /**
     * Хранилище состояний чатов.
     */
  //  private final ChatStateHolder chatStateHolder;
    private final ChatStateService chatStateService;

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
    //    BotCommand currentState = chatStateHolder.getCurrentStateById(chatId);
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == DOG || currentState == CAT || currentState == SHELTER_INFO) {
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
        //    chatStateHolder.addState(chatId, SHELTER_INFO);
            chatStateService.updateChatState(chatId, SHELTER_INFO);
        } else if (currentState == STOP) {
            String responseText = "Для использования бота введите команду /start";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
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