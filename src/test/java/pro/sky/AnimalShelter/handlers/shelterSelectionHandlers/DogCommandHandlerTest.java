package pro.sky.AnimalShelter.handlers.shelterSelectionHandlers;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.SHELTER_COMMAND_TEXT;

@ExtendWith(MockitoExtension.class)
class DogCommandHandlerTest {
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
    private DogCommandHandler dogCommandHandler;

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
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(DOG);
        dogCommandHandler.handle(update);
        var shelter = currentState == CAT ? "приют для кошек" : "приют для собак";
        String responseText = "Вы уже выбрали " + shelter + ".\n" +
                " Для возврата в предыдущее меню введите команду назад /back,\n" +
                " Чтобы выключить бота введите команду /stop";
        SendMessage message = new SendMessage(chatId.toString(), responseText);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }
    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса DogCommandHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        dogCommandHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса DogCommandHandler \" +\n"
            + "в состоянии Назад вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        dogCommandHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса DogCommandHandler возвращает правильную команду /dog")
    public void testGetCommand() {
        BotCommand actualCommand = dogCommandHandler.getCommand();
        assertEquals(DOG, actualCommand);
    }

}