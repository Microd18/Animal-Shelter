package pro.sky.AnimalShelter.handlers.sendingReportMenuHandlers;

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
import pro.sky.AnimalShelter.service.UserReportStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.PET_REPORT_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.SEND_REPORT_TEXT;

@ExtendWith(MockitoExtension.class)
class SendReportCommandHandlerTest {
    @Mock
    private ChatStateService chatStateService;

    @Mock
    private TelegramBot telegramBot;
    @Mock
    private UserReportStateService userReportStateService;

    @Mock
    private CommonUtils commonUtils;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @InjectMocks
    private SendReportCommandHandler sendReportCommandHandler;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }
    @Test
    @DisplayName("Проверяет, что при выполнении команды /send_report, если текущее состояние чата " +
            "(chatId) равно /send_report, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleStateSendPetReportCorrectMessage() {
        Long chatId = 123L;
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(PET_REPORT);
        sendReportCommandHandler.handle(update);
        String s = currentState == SEND_REPORT ? "Вы уже в этом меню." : "";
        String responseText = s + SEND_REPORT_TEXT;
        SendMessage message = new SendMessage(chatId.toString(), responseText);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }
    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса SendReportCommandHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        sendReportCommandHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса SendReportCommandHandler \" +\n"
            + "в состоянии Назад вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        sendReportCommandHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса SendReportCommandHandler возвращает правильную команду BotCommand.DATING_RULES")
    public void testGetCommand() {
        BotCommand actualCommand = sendReportCommandHandler.getCommand();
        assertEquals(SEND_REPORT, actualCommand);
    }


}