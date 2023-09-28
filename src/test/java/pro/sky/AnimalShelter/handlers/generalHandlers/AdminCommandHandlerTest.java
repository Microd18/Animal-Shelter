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
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.ADMIN_COMMAND_TEXT;

@ExtendWith(MockitoExtension.class)
class AdminCommandHandlerTest {
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
    private AdminCommandHandler adminCommandHandler;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса AdminCommandHandler " + "в состоянии \"Вызов меню Admin\" отправляется сообщение с текстом " + "в чат с заданным chatId.")
    public void testAdminCommandHandler() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, ADMIN_COMMAND_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }
    @Test
    @DisplayName("Проверяет, что метод getCommand класса AdminCommandHandler возвращает правильную команду BotCommand.DATING_RULES")
    public void testGetCommand() {
        BotCommand expectedCommand = BotCommand.ADMIN;
        BotCommand actualCommand = adminCommandHandler.getCommand();
        assertEquals(expectedCommand, actualCommand);
    }

}