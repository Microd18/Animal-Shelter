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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.service.ChatStateService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.ADMIN_COMMAND_TEXT;

@ExtendWith(MockitoExtension.class)
class AdminCommandHandlerTest {
    @Mock
    private ChatStateService chatStateService;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private TelegramBot telegramBot;

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
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса AdminCommandHandler возвращает правильную команду ADMIN")
    public void testGetCommand() {
        BotCommand actualCommand = adminCommandHandler.getCommand();
        assertEquals(ADMIN, actualCommand);
    }


    @Test
    @DisplayName("Проверка обработку команды, когда текущее состояние не ADMIN, существующий чат")
    public void testHandleNonAdminWithExistingChat() {
        pro.sky.AnimalShelter.entity.Chat chat1 = new pro.sky.AnimalShelter.entity.Chat();
        chat1.setChatId(123L);

        when(chatStateService.getCurrentStateByChatId(anyLong())).thenReturn(STOP);
        when(chatRepository.findByChatId(anyLong())).thenReturn(Optional.of(chat1));

        adminCommandHandler.handle(update);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Введите пароль:", sendMessage.getParameters().get("text"));
        assertEquals(123L, sendMessage.getParameters().get("chat_id"));

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(chatRepository, times(1)).save(chat1);
        verify(chatStateService, times(1)).updateChatState(123L, CHECK_ADMIN_PASSWORD);
    }

    @Test
    @DisplayName("Проверка обработку команды, когда текущее состояние не ADMIN, несуществующий чат")
    public void testHandleNonAdminWithNonExistingChat() {
        when(chatStateService.getCurrentStateByChatId(anyLong())).thenReturn(STOP);
        when(chatRepository.findByChatId(anyLong())).thenReturn(Optional.empty());

        adminCommandHandler.handle(update);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Введите пароль:", sendMessage.getParameters().get("text"));
        assertEquals(123L, sendMessage.getParameters().get("chat_id"));

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(chatRepository, times(1)).save(any(pro.sky.AnimalShelter.entity.Chat.class));
        verify(chatStateService, times(1)).updateChatState(123L, CHECK_ADMIN_PASSWORD);
    }

    @Test
    @DisplayName("Проверка обработку команды, когда текущее состояние ADMIN")
    public void testHandleAdminState() {
        when(chatStateService.getCurrentStateByChatId(anyLong())).thenReturn(ADMIN);

        adminCommandHandler.handle(update);

        verify(chatStateService, times(1)).getCurrentStateByChatId(anyLong());
        verify(chatStateService, never()).updateChatState(anyLong(), any());
        verifyNoInteractions(telegramBot, chatRepository);
    }
}

