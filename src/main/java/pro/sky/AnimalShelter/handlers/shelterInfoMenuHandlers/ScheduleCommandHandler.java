package pro.sky.AnimalShelter.handlers.shelterInfoMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static pro.sky.AnimalShelter.enums.BotCommand.SCHEDULE;
import static pro.sky.AnimalShelter.enums.BotCommand.SHELTER_INFO;

/**
 * Обработчик команды "/schedule".
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleCommandHandler implements CommandHandler {

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

    Logger logger = LoggerFactory.getLogger(ScheduleCommandHandler.class);

    /**
     * Обрабатывает команду "/schedule" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == SHELTER_INFO) {
            String responseText = "Мы находимя по адресу:\n" +
                    "ул. Аккорган, 5В, микрорайон Коктал, Астана\n" +
                    "телефон для связи: +7(123)4567890\n" +
                    "Расписание работы приюта: \n" +
                    "Понедельник 09:00–16:00 \n" +
                    "Вторник 09:00–16:00 \n" +
                    "Среда 09:00–16:00 \n" +
                    "Четверг 09:00–16:00 \n" +
                    "Пятница 09:00–16:00 \n" +
                    "Суббота 09:00–16:00 \n" +
                    "Воскресенье 09:00–16:00\n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        } else {
            commonUtils.offerToStart(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/schedule").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return SCHEDULE;
    }
}