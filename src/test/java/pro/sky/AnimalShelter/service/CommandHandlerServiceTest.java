package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.listener.TelegramBotUpdatesListener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommandHandlerServiceTest {
    private final TelegramBot telegramBot = Mockito.mock(TelegramBot.class);
    @Mock
    CommandHandlerService commandHandlerService;
    @Mock
    TelegramBotUpdatesListener telegramBotUpdatesListener;

    @Test
    public void Test1() {
        Assertions.assertThat(telegramBot).isNotNull();
    }

    @Test
    public void Test2() {
        Assertions.assertThat(commandHandlerService).isNotNull();
    }

    @Test
    public void Test3() {
        commandHandlerService.process(any());
        verify(commandHandlerService, times(1)).process(any());
        Assertions.assertThat(telegramBotUpdatesListener.process(anyList())).isNotNull();
    }
}