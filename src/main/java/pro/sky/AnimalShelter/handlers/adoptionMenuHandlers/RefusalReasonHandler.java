package pro.sky.AnimalShelter.handlers.adoptionMenuHandlers;

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

@Service
@RequiredArgsConstructor
public class RefusalReasonHandler implements CommandHandler {
    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Экземпляр утилитарного класс для общих методов.
     */
    private final CommonUtils commonUtils;

    /**
     * Обрабатывает команду "/refusal_reason" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == ADOPT) {
            String responseText = "    5 причин отказа забрать питомца из приюта:\n" +
                    "1.Большое количество животных дома.\n" +
                    "2.Нестабильные отношения в семье\n" +
                    "3.Наличие маленьких детей\n" +
                    "4.Съемное жилье\n" +
                    "5.Животное в подарок или для работы\n" +
                    "             \n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);

        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/refusal_reason").
     *
     * @return Команда, связанная с обработчиком.
     */

    @Override
    public BotCommand getCommand() {
        return REFUSAL_REASON;
    }
}
