package pro.sky.AnimalShelter.handlers.volunteerMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.repository.UserRepository;
import pro.sky.AnimalShelter.service.ChatStateService;

import static pro.sky.AnimalShelter.enums.BotCommand.ADMIN;
import static pro.sky.AnimalShelter.enums.BotCommand.COMPLETED_PROBATION_ADOPTERS;

/**
 * Обработчик команды "/without_report_adopters".
 */
@Service
@RequiredArgsConstructor
public class CompletedProbationAdoptersCommandHandler implements CommandHandler {

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
            userRepository.findUsersWithOver30Days().forEach(user -> {
                String catString = user.getVolunteerInfoCat() == null ? "" : String.format(
                        "Дни испытательного срока для кошки: %d\n" +
                                "Рейтинг отчетов по кошке: %.1f\n" +
                                "Добавленные дни к испытательному сроку: %d\n",
                        user.getVolunteerInfoCat().getAmountOfDays(),
                        user.getVolunteerInfoCat().getRating(),
                        user.getVolunteerInfoCat().getExtraDays());

                String dogString = user.getVolunteerInfoDog() == null ? "" : String.format(
                        "Дни испытательного срока для собаки: %d\n" +
                                "Рейтинг отчетов по собаке: %.1f\n" +
                                "Добавленные дни к испытательному сроку: %d\n",
                        user.getVolunteerInfoDog().getAmountOfDays(),
                        user.getVolunteerInfoDog().getRating(),
                        user.getVolunteerInfoDog().getExtraDays());
                telegramBot.execute(new SendMessage(chatId, user + "\n" + catString + dogString));
            });
        } else {
            telegramBot.execute(new SendMessage(chatId, "Сперва зайдите в меню волонтёра"));
        }
    }

    @Override
    public BotCommand getCommand() {
        return COMPLETED_PROBATION_ADOPTERS;
    }
}
