package pro.sky.AnimalShelter.handlers.sendingReportMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.service.UserReportStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.enums.UserReportStates.PHOTO;


/**
 * Обработчик команды "/send_report".
 */
@Service
@RequiredArgsConstructor
public class SendReportCommandHandler implements CommandHandler {

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    private final UserReportStateService userReportStateService;

    /**
     * Экземпляр утилитарного класс для общих методов.
     */
    private final CommonUtils commonUtils;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Обрабатывает команду "/send_report" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == PET_REPORT || currentState == SEND_REPORT) {
            String s = currentState == SEND_REPORT ? "Вы уже в этом меню. " : "";
            String responseText = s + "Отправьте фото:\n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";

            telegramBot.execute(new SendMessage(chatId.toString(), responseText));
            if (!(currentState == SEND_REPORT)) {
                chatStateService.updateChatState(chatId, SEND_REPORT);
            }
            userReportStateService.updateUserReportState(chatId, PHOTO);
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/send_report").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return SEND_REPORT;
    }
}
