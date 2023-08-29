package pro.sky.AnimalShelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.service.CommandHandlerService;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final CommandHandlerService commandHandlerService;

    private final TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    @Transactional
    public int process(final List<Update> updates) {
        updates.forEach(commandHandlerService::process);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}