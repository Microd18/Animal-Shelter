package pro.sky.AnimalShelter.handlers.volunteerMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.service.VolunteerService;

import static pro.sky.AnimalShelter.enums.BotCommand.ADMIN;
import static pro.sky.AnimalShelter.enums.BotCommand.ALL_ADOPTERS;


/**
 * Обработчик команды "/all_adopters".
 */
@Service
@RequiredArgsConstructor
public class AllAdoptersCommandHandler implements CommandHandler {

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Сервис для обработки команд меню волонтера.
     */
    private final VolunteerService volunteerService;


    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Обрабатывает команду "/all_adopters" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == ADMIN) {
            volunteerService.getAllAdopters(chatId);
        } else {
            telegramBot.execute(new SendMessage(chatId, "Сперва зайдите в меню волонтёра"));
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/all_adopters").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return ALL_ADOPTERS;
    }
}

