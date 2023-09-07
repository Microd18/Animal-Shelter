package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.state.ChatStateHolder;

import static pro.sky.AnimalShelter.enums.BotCommand.CAT;

/**
 * Обработчик команды "/cat". Предоставляет доступ к информации о приюте для кошек.
 * Расширяет абстрактный класс ShelterCommandHandler, который обобщает обработку команд для приютов.
 */
@Service
public class CatCommandHandler extends ShelterCommandHandler {

    /**
     * Создает экземпляр обработчика команды "/cat".
     *
     * @param chatStateHolder Хранилище состояний чатов.
     * @param chatStateService Сервис для управления очередью состояний чатов.
     * @param telegramBot     Telegram бот.
     */
    public CatCommandHandler(ChatStateHolder chatStateHolder, ChatStateService chatStateService, TelegramBot telegramBot) {
        super(chatStateHolder, chatStateService, telegramBot, CAT, "приют для кошек");
    }

    /**
     * Получает команду, обрабатываемую этим обработчиком.
     *
     * @return Команда обработчика.
     */
    @Override
    public BotCommand getCommand() {
        return CAT;
    }
}
