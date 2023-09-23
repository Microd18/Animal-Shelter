package pro.sky.AnimalShelter.handlers.adoptionMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static pro.sky.AnimalShelter.messages.MessagesBot.datingRulesHandlerCat;
import static pro.sky.AnimalShelter.messages.MessagesBot.datingRulesHandlerDog;

public class DatingRulesHandlerTest {
    @Mock
    private ChatStateService chatStateService;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private CommonUtils commonUtils;

    @Before
    public void setUp() {
        initMocks(this);
        AdoptCommandHandler adoptCommandHandler = new AdoptCommandHandler(chatStateService, telegramBot, commonUtils);
    }
    @Test
    public void testDatingRulesHandlerDog() {
        Long chatId = 123L;
        BotCommand currentState = BotCommand.DATING_RULES;

        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(currentState);
        SendMessage message = new SendMessage(chatId.toString(), datingRulesHandlerDog);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }
    @Test
    public void testDatingRulesHandlerCat() {
        Long chatId = 123L;
        BotCommand currentState = BotCommand.DATING_RULES;

        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(currentState);
        SendMessage message = new SendMessage(chatId.toString(), datingRulesHandlerCat);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }
}
