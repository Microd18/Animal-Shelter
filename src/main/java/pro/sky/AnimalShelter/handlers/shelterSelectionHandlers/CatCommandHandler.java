package pro.sky.AnimalShelter.handlers.shelterSelectionHandlers;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.state.ChatStateHolder;
import pro.sky.AnimalShelter.utils.CommonUtils;

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
     * @param telegramBot     Telegram бот.
     */
    public CatCommandHandler(ChatStateHolder chatStateHolder, TelegramBot telegramBot, CommonUtils commonUtils) {
        super(chatStateHolder, telegramBot, commonUtils, CAT, "приют для кошек");
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
