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
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.PET_REPORT_TEXT;

@ExtendWith(MockitoExtension.class)
class PetReportCommandHandlerTest {
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
    private PetReportCommandHandler petReportCommandHandler;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, что при выполнении команды /pet_report, если текущее состояние чата " +
            "(chatId) равно /dog, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleStateDogSendCorrectMessage() {
        Long chatId = 123L;
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(DOG);
        petReportCommandHandler.handle(update);
        String s = currentState == PET_REPORT ? "Вы уже в этом меню." : "";
        String responseText = s + PET_REPORT_TEXT;
        SendMessage message = new SendMessage(chatId.toString(), responseText);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при выполнении команды /pet_report, если текущее состояние чата " +
            "(chatId) равно /cat, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleStateSendCatCorrectMessage() {
        Long chatId = 123L;
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(CAT);
        petReportCommandHandler.handle(update);
        String s = currentState == PET_REPORT ? "Вы уже в этом меню." : "";
        String responseText = s + PET_REPORT_TEXT;
        SendMessage message = new SendMessage(chatId.toString(), responseText);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при выполнении команды /pet_report, если текущее состояние чата " +
            "(chatId) равно /pet_report, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleStateSendPetReportCorrectMessage() {
        Long chatId = 123L;
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(PET_REPORT);
        petReportCommandHandler.handle(update);
        String s = currentState == PET_REPORT ? "Вы уже в этом меню." : "";
        String responseText = s + PET_REPORT_TEXT;
        SendMessage message = new SendMessage(chatId.toString(), responseText);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса PetReportCommandHandler отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testPetReportCommandHandler() {
        Long chatId = 123L;
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        String s = currentState == PET_REPORT ? "Вы уже в этом меню." : "";
        String responseText = s + PET_REPORT_TEXT;
        SendMessage message = new SendMessage(chatId.toString(), responseText);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса PetReportCommandHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        petReportCommandHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса PetReportCommandHandler \" +\n"
            + "в состоянии Назад вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        petReportCommandHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса PetReportCommandHandler возвращает правильную команду /pet_report")
    public void testGetCommand() {
        BotCommand actualCommand = petReportCommandHandler.getCommand();
        assertEquals(PET_REPORT, actualCommand);
    }


}