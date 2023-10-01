package pro.sky.AnimalShelter.handlers.volunteerMenuHandlers;

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
import pro.sky.AnimalShelter.service.CheckUserReportService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.ADMIN;
import static pro.sky.AnimalShelter.enums.BotCommand.CHECK_REPORT;
import static pro.sky.AnimalShelter.utils.MessagesBot.*;

@ExtendWith(MockitoExtension.class)
class CheckReportCommandHandlerTest {
    @Mock
    private ChatStateService chatStateService;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private CheckUserReportService checkUserReportService;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @InjectMocks
    private CheckReportCommandHandler checkReportCommandHandler;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, что при выполнении команды /check_report, если текущее состояние чата " +
            "(chatId) равно /admin ,, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleStateSendCorrectMessage() {
        Long chatId = 123L;
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(ADMIN);
        checkReportCommandHandler.handle(update);
        String responseText = currentState == CHECK_REPORT ? CHECK_REPORT_IN_PROGRESS_TEXT : CHECK_REPORT_START_TEXT;
        SendMessage message = new SendMessage(chatId.toString(), responseText);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса CheckReportCommandHandler " +
            "в состоянии \"Меню Admin\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testFindAnimalByNameCommandHandler() {
        Long chatId = 123L;
        SendMessage message = new SendMessage(chatId, WAITING_ANIMAL_NAME_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса CheckReportCommandHandler \" +\n"
            + "в состоянии \\\"Назад\\\" вызывается метод execute класса TelegramBot.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);
        checkReportCommandHandler.handle(update);
        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса CheckReportCommandHandler возвращает правильную команду /check_report")
    public void testGetCommand() {
        BotCommand actualCommand = checkReportCommandHandler.getCommand();
        assertEquals(CHECK_REPORT, actualCommand);
    }

}