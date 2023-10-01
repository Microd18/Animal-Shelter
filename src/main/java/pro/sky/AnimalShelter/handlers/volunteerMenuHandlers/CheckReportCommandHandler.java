package pro.sky.AnimalShelter.handlers.volunteerMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.service.CheckUserReportService;

import static pro.sky.AnimalShelter.enums.BotCommand.ADMIN;
import static pro.sky.AnimalShelter.enums.BotCommand.CHECK_REPORT;
import static pro.sky.AnimalShelter.utils.MessagesBot.CHECK_REPORT_IN_PROGRESS_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.CHECK_REPORT_START_TEXT;

/**
 * Обработчик команды "/check_report".
 */
@Service
@RequiredArgsConstructor
public class CheckReportCommandHandler implements CommandHandler {

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Сервис для обработки состояний просмотра и проверки отчета.
     */
    private final CheckUserReportService checkUserReportService;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Обрабатывает команду "/check_report" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == ADMIN || currentState == CHECK_REPORT) {
            String responseText = currentState == CHECK_REPORT ? CHECK_REPORT_IN_PROGRESS_TEXT : CHECK_REPORT_START_TEXT;
            telegramBot.execute(new SendMessage(chatId.toString(), responseText));
            if (!(currentState == CHECK_REPORT)) {
                chatStateService.updateChatState(chatId, CHECK_REPORT);
            }
            checkUserReportService.init(chatId);
        } else {
            telegramBot.execute(new SendMessage(chatId, "Сперва зайдите в меню волонтёра"));
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/check_report").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return CHECK_REPORT;
    }
}
