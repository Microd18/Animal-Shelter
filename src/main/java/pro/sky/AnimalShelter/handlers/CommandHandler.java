package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.model.Update;
import pro.sky.AnimalShelter.enums.BotCommand;

public interface CommandHandler {
    void handle(Update update);

    BotCommand getCommand();

}
