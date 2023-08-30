package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.state.ChatStateHolder;

import static pro.sky.AnimalShelter.enums.BotCommand.CAT;

@Service
public class CatCommandHandler extends ShelterCommandHandler {

    public CatCommandHandler(ChatStateHolder chatStateHolder, TelegramBot telegramBot) {
        super(chatStateHolder, telegramBot, BotCommand.CAT, "приют для кошек");
    }

    @Override
    public BotCommand getCommand() {
        return CAT;
    }
}
