package pro.sky.AnimalShelter.shelterInfoMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.shelterInfoMenuHandlers.PassCommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.DOG_SHELTER_DESCRIPTION_TEXT;

@ExtendWith(MockitoExtension.class)
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

    @InjectMocks
    private PassCommandHandler passCommandHandler;

    @BeforeEach
    public void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }
    @Test
    @DisplayName("Проверяет, что при выполнении команды /pass, если текущее состояние чата " +
            "(chatId) равно /shelter_info, и предыдущее состояние чата равно " +
            "/DOG, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleDogStateSendCorrectMessage() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(SHELTER_INFO);
        when(chatStateService.getPreviousStateByChatId(123L)).thenReturn(DOG);
        passCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId, PASS_COMMAND_DOG_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }
    @Test
    @DisplayName("Проверяет, что при выполнении команды /pass, если текущее состояние чата " +
            "(chatId) равно /shelter_info, и предыдущее состояние чата равно " +
            "/CAT, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleCatStateSendCorrectMessage() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(SHELTER_INFO);
        when(chatStateService.getPreviousStateByChatId(123L)).thenReturn(CAT);
        passCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId, PASS_COMMAND_CAT_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса PassCommandHandler " +
            "в состоянии \"Контактные данные для охраны\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testPassCommandHandlerDog() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, PASS_COMMAND_DOG_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса PassCommandHandler " +
            "в состоянии \"Контактные данные для охраны\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testPassCommandHandlerCat() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, PASS_COMMAND_CAT_TEXT);
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
