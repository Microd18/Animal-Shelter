package pro.sky.AnimalShelter.handlers.shelterSelectionHandlers;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.state.ChatStateHolder;

import static pro.sky.AnimalShelter.enums.BotCommand.DOG;

/**
 * Обработчик команды "/dog". Предоставляет доступ к информации о приюте для собак.
 * Расширяет абстрактный класс ShelterCommandHandler, который обобщает обработку команд для приютов.
 */
@Service
public class DogCommandHandler extends ShelterCommandHandler {

    /**
     * Создает экземпляр обработчика команды "/dog".
     *
     * @param chatStateHolder  Хранилище состояний чатов.
     * @param chatStateService Сервис для управления очередью состояний чатов.
     * @param telegramBot      Telegram бот.
     */
    public DogCommandHandler(ChatStateHolder chatStateHolder, ChatStateService chatStateService, TelegramBot telegramBot) {
        super(chatStateHolder, chatStateService, telegramBot, DOG, "приют для собак");
    }

    /**
     * Получает команду, обрабатываемую этим обработчиком.
     *
     * @return Команда обработчика.
     */
    @Override
    public BotCommand getCommand() {
        return DOG;
    }
}
