package pro.sky.AnimalShelter.handlers.generalHandlers;

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
import pro.sky.AnimalShelter.service.ChatService;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.START;
import static pro.sky.AnimalShelter.enums.BotCommand.STOP;
import static pro.sky.AnimalShelter.utils.MessagesBot.START_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.STOP_TEXT;

@ExtendWith(MockitoExtension.class)
class StopCommandHandlerTest {
    @Mock
    private ChatStateService chatStateService;
    @Mock
    private ChatService chatService;

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
    private StopCommandHandler stopCommandHandler;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }
    @Test
    @DisplayName("Проверка на остановку чата")
    public void testStopHandler() {
        Long chatId = 123L;
        stopCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId.toString(), STOP_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
        verify(chatStateService, times(1)).stopBot(chatId);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса AdminCommandHandler возвращает правильную команду BotCommand.DATING_RULES")
    public void testGetCommand() {
        BotCommand actualCommand = stopCommandHandler.getCommand();
        assertEquals(STOP, actualCommand);
    }
}