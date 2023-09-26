package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.enums.UserReportStates;
import pro.sky.AnimalShelter.handlers.CommandHandler;

import java.util.List;
import java.util.stream.Collectors;

import static pro.sky.AnimalShelter.enums.BotCommand.CONTACT;
import static pro.sky.AnimalShelter.enums.BotCommand.SEND_REPORT;
import static pro.sky.AnimalShelter.enums.UserReportStates.*;

/**
 * Сервис для обработки команд.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CommandHandlerService {

    private final UserService userService;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Сервис для управления состоянием отчетов о пользователях.
     */
    private final UserReportStateService userReportStateService;

    /**
     * Сервис для работы с отчетами о пользователях.
     */
    private final UserReportService userReportService;

    /**
     * Список обработчиков команд.
     */
    private final List<CommandHandler> commandHandlers;

    /**
     * Обработка входящего обновления.
     *
     * @param update Обновление от Telegram.
     */
    public void process(Update update) {
        Message message = update.message();
        if (message == null)
            return;

        Long chatId = message.chat().id();
        UserReportStates reportCurrentState = userReportStateService.getCurrentStateByChatId(chatId);
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        String messageText = message.text();

        if (messageText != null) {
            List<CommandHandler> matchedHandlers = commandHandlers.stream()
                    .filter(commandHandler -> commandHandler.getCommand().getCommandText().equals(messageText))
                    .collect(Collectors.toList());

            if (!matchedHandlers.isEmpty()) {
                matchedHandlers.get(0).handle(update);
                return;
            }

            if (currentState == CONTACT) {
                userService.updateContact(chatId, messageText);
                return;
			}	
			if (currentState == FIND_USER_BY_PHONE) {
                    volunteerService.findUsersByPhone(chatId, messageText);
                    return;
            }
            if (currentState == FIND_ANIMAL_BY_NAME) {
                    volunteerService.findAnimalByName(chatId, messageText);
                    return;
            }
            if (currentState == MAKE_ADOPTER) {
                    volunteerService.allowUserBecomeAdopter(chatId, messageText);
                    return;
            }           	          
            if (currentState == SEND_REPORT && (reportCurrentState == RATION || reportCurrentState == WELL_BEING || reportCurrentState == BEHAVIOR)) {
                userReportService.saveReportData(chatId, messageText, reportCurrentState);
                return;
            }
        }
        if (currentState == SEND_REPORT && reportCurrentState == PHOTO && message.photo() != null) {
            userReportService.savePhotoForReport(chatId, message.photo());
            return;
        }
        handleUnknownCommand(update);
    }

    /**
     * Обработка неизвестной команды.
     *
     * @param update Обновление от Telegram.
     */
    private void handleUnknownCommand(Update update) {
        Message message = update.message();
        telegramBot.execute(new SendMessage(message.chat().id(), "Неизвестная команда"));
    }
}
