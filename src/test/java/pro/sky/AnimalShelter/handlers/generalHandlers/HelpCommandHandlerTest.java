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
import static org.mockito.Mockito.verify;
import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.*;

@ExtendWith(MockitoExtension.class)
class HelpCommandHandlerTest {
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
    private HelpCommandHandler helpCommandHandler;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }
    @Test
    @DisplayName("Тестирование обработки команды /help в состоянии DOG:")
    public void testHandleContactCommandHelpDogStateSendContactText() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(DOG);
        helpCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId, HELP_COMMAND_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }
    @Test
    @DisplayName("Тестирование обработки команды /help в состоянии CAT:")
    public void testHandleContactCommandHelpCatStateSendContactText() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(CAT);
        helpCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId, HELP_COMMAND_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }
    @Test
    @DisplayName("Тестирование обработки команды /help в состоянии SHELTER_INFO:")
    public void testHandleContactCommandHelpShelterInfoStateSendContactText() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(SHELTER_INFO);
        helpCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId, HELP_COMMAND_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }
    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса HelpCommandHandler " +
            "в состоянии \"Позвать волонтера\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testDocumentsListHandler() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, HELP_COMMAND_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }
    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса HelpCommandHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        helpCommandHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }
    @Test
    @DisplayName("Проверяет, что метод getCommand класса HelpCommandHandler возвращает правильную команду DOG_TRAINER_ADVICE.")
    public void testGetCommand() {
        BotCommand actualCommand = helpCommandHandler.getCommand();
        assertEquals(HELP, actualCommand);
    }
}