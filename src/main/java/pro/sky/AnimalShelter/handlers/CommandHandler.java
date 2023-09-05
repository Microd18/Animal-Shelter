package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.model.Update;
import pro.sky.AnimalShelter.enums.BotCommand;

/**
 * Интерфейс, представляющий обработчик команды для телеграм-бота.
 */
public interface CommandHandler {

    /**
     * Метод для обработки команды на основе полученного объекта Update.
     *
     * @param update Объект Update, содержащий информацию о команде.
     */
    void handle(Update update);

    /**
     * Метод, возвращающий команду, которую обрабатывает данный обработчик.
     *
     * @return Команда, обрабатываемая данным обработчиком.
     */
    BotCommand getCommand();
}