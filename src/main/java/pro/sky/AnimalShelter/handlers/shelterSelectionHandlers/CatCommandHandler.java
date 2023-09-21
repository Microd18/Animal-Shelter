package pro.sky.AnimalShelter.handlers.shelterSelectionHandlers;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.service.ChatStateService;
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
     * @param chatStateService Сервис для управления очередью состояний чатов.
     * @param commonUtils      Утилитарный класс для общих методов.
     * @param telegramBot      Telegram бот.
     */
    public CatCommandHandler(ChatStateService chatStateService, CommonUtils commonUtils, TelegramBot telegramBot) {
        super(chatStateService, commonUtils, telegramBot, CAT, "приют для кошек");
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
