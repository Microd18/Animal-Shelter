package pro.sky.AnimalShelter.handlers.sendingReportMenuHandlers;

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
 * Обработчик команды "/pet_report".
 */
@Service
@RequiredArgsConstructor
public class PetReportCommandHandler implements CommandHandler {

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
     * Обрабатывает команду "/pet_report" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == DOG || currentState == CAT || currentState == PET_REPORT) {
            String s = currentState == PET_REPORT ? "Вы уже в этом меню." : "";
            String responseText = s + "Это меню для отправки отчёта, выберите действие:\n" +
                    "1. Отправить отчёт: выберите этот пункт меню, чтобы отправить ежедневный отчёт о вашем питомце (/send_report)\n" +
                    "2. Посмотреть шаблон для отчёта: если вам нужно ознакомиться с шаблоном для ежедневного отчёта перед его отправкой, выберите этот пункт меню (/report_template)\n" +
                    "3. Назад (/back)\n" +
                    "4. Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);

            if (!(currentState == PET_REPORT)) {
                chatStateService.updateChatState(chatId, PET_REPORT);
            }
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/pet_report").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return PET_REPORT;
    }
}
