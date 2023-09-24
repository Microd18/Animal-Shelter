package pro.sky.AnimalShelter.adoptionMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.adoptionMenuHandlers.DatingRulesHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static pro.sky.AnimalShelter.utils.MessagesBot.DATING_RULES_CAT_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.DATING_RULES_DOG_TEXT;

public class DatingRulesHandlerTest {
    @Mock
    private ChatStateService chatStateService;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private CommonUtils commonUtils;
    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    private DatingRulesHandler datingRulesHandler;

    @Before
    public void setUp() {
        initMocks(this);
        datingRulesHandler = new DatingRulesHandler(chatStateService, telegramBot, commonUtils);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса DatingRulesHandler " +
            "в состоянии \"Правила знакомства с животным\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testDatingRulesHandlerDog() {
        Long chatId = 123L;
        BotCommand currentState = BotCommand.DATING_RULES;

        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(currentState);
        SendMessage message = new SendMessage(chatId.toString(), DATING_RULES_DOG_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса DatingRulesHandler " +
            "в состоянии \"Правила знакомства с животным\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testDatingRulesHandlerCat() {
        Long chatId = 123L;
        BotCommand currentState = BotCommand.DATING_RULES;

        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(currentState);
        SendMessage message = new SendMessage(chatId.toString(), DATING_RULES_CAT_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса DatingRulesHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        datingRulesHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса DatingRulesHandler \" +\n"
            + "в состоянии \\\"Назад\\\" вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        datingRulesHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса DatingRulesHandler возвращает правильную команду BotCommand.DATING_RULES")
    public void testGetCommand() {
        BotCommand expectedCommand = BotCommand.DATING_RULES;
        BotCommand actualCommand = datingRulesHandler.getCommand();
        assertEquals(expectedCommand, actualCommand);
    }
}
