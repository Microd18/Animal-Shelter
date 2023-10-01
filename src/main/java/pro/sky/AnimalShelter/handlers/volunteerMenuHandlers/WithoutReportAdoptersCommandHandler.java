package pro.sky.AnimalShelter.handlers.volunteerMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.entity.User;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.repository.UserRepository;
import pro.sky.AnimalShelter.service.ChatStateService;

import java.util.List;

import static pro.sky.AnimalShelter.enums.BotCommand.ADMIN;
import static pro.sky.AnimalShelter.enums.BotCommand.WITHOUT_REPORT_ADOPTERS;

/**
 * Обработчик команды "/without_report_adopters".
 */
@Service
@RequiredArgsConstructor
public class WithoutReportAdoptersCommandHandler implements CommandHandler {

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Репозиторий для хранения юзеров.
     */
    private final UserRepository userRepository;

    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == ADMIN) {
            List<User> usersWithOldReports = userRepository.findUsersWithOldReports();
            SendMessage message = new SendMessage(chatId, usersWithOldReports.toString());
            telegramBot.execute(message);
        } else {
            telegramBot.execute(new SendMessage(chatId, "Сперва зайдите в меню волонтёра"));
        }
    }

    @Override
    public BotCommand getCommand() {
        return WITHOUT_REPORT_ADOPTERS;
    }
}
