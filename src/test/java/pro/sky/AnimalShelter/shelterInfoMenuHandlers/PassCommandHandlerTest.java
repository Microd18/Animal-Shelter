package pro.sky.AnimalShelter.shelterInfoMenuHandlers;

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
import pro.sky.AnimalShelter.handlers.shelterInfoMenuHandlers.PassCommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static pro.sky.AnimalShelter.utils.MessagesBot.PASS_COMMAND_CAT_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.PASS_COMMAND_DOG_TEXT;

public class PassCommandHandlerTest {
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

    private PassCommandHandler passCommandHandler;

    @Before
    public void setUp() {
        initMocks(this);
        passCommandHandler = new PassCommandHandler(chatStateService, telegramBot, commonUtils);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса PassCommandHandler " +
            "в состоянии \"Контактные данные для охраны\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testPassCommandHandlerDog() {
        Long chatId = 123L;
        BotCommand currentState = BotCommand.PASS;

        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(currentState);
        SendMessage message = new SendMessage(chatId.toString(), PASS_COMMAND_DOG_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса PassCommandHandler " +
            "в состоянии \"Контактные данные для охраны\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testPassCommandHandlerCat() {
        Long chatId = 123L;
        BotCommand currentState = BotCommand.PASS;

        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(currentState);
        SendMessage message = new SendMessage(chatId.toString(), PASS_COMMAND_CAT_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса PassCommandHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        passCommandHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса PassCommandHandler \" +\n"
            + "в состоянии \\\"Назад\\\" вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        passCommandHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса PassCommandHandler возвращает правильную команду BotCommand.PASS")
    public void testGetCommand() {
        BotCommand expectedCommand = BotCommand.PASS;
        BotCommand actualCommand = passCommandHandler.getCommand();
        assertEquals(expectedCommand, actualCommand);
    }
}
