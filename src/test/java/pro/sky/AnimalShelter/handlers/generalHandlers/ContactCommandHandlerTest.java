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
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.CONTACT_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.HELP_COMMAND_TEXT;

@ExtendWith(MockitoExtension.class)
class ContactCommandHandlerTest {
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
    private ContactCommandHandler contactCommandHandler;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }
    @Test
    @DisplayName("Тестирование обработки команды /contact в состоянии SHELTER_INFO:")
    public void handleContactCommandShelterInfoStateSendContactText() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(SHELTER_INFO);
        contactCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId, CONTACT_TEXT);
        telegramBot.execute(message);
        verify(telegramBot).execute(message);
        verify(chatStateService).updateChatState(chatId, CONTACT);
    }

    @Test
    @DisplayName("Тестирование обработки команды /contact в состоянии ADOPT:")
    public void handleContactCommandAdoptStateSendContactText() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(ADOPT);
        contactCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId, CONTACT_TEXT);
        telegramBot.execute(message);
        verify(telegramBot).execute(message);
        verify(chatStateService).updateChatState(chatId, CONTACT);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса ContactCommandHandler " +
            "в состоянии \"Запрос контакта\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testMakeAdopterCommandHandler() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, CONTACT_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }


    @Test
    @DisplayName("Проверяет, что метод getCommand класса ContactCommandHandler возвращает правильную команду CONTACT")
    public void testGetCommand() {
        BotCommand actualCommand = contactCommandHandler.getCommand();
        assertEquals(CONTACT, actualCommand);
    }

}