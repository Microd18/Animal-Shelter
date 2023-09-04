package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.state.ChatStateHolder;

import static pro.sky.AnimalShelter.enums.BotCommand.DOG;

@Service
public class DogCommandHandler extends ShelterCommandHandler {

    public DogCommandHandler(ChatStateHolder chatStateHolder, TelegramBot telegramBot) {
        super(chatStateHolder, telegramBot, DOG, "приют для собак");
    }

    @Override
    public BotCommand getCommand() {
        return DOG;
    }
}
