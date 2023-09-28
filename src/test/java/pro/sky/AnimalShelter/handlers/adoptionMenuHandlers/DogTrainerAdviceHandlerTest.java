package pro.sky.AnimalShelter.handlers.adoptionMenuHandlers;

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
import static pro.sky.AnimalShelter.utils.MessagesBot.DOG_TRAINER_ADVICE_TEXT;

@ExtendWith(MockitoExtension.class)
public class DogTrainerAdviceHandlerTest {

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
    private DogTrainerAdviceHandler dogTrainerAdviceHandler;

    @BeforeEach
    public void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, что при выполнении команды /dog_trainer_advice, если текущее состояние чата " +
            "(chatId) равно /ADOPT, и предыдущее состояние чата равно " +
            "/DOG, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleDogStateSendCorrectMessage() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.ADOPT);
        when(chatStateService.getPreviousStateByChatId(123L)).thenReturn(BotCommand.DOG);
        dogTrainerAdviceHandler.handle(update);
        SendMessage message = new SendMessage(chatId, DOG_TRAINER_ADVICE_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса DogTrainerAdviceHandler " +
            "в состоянии \"Советы кинолога\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testDogTrainerAdviceHandler() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, DOG_TRAINER_ADVICE_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса DogTrainerAdviceHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        dogTrainerAdviceHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса DogTrainerAdviceHandler \" +\n"
            + "в состоянии \\\"Назад\\\" вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        dogTrainerAdviceHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса DogTrainerAdviceHandler возвращает правильную команду BotCommand.DOG_TRAINER_ADVICE.")
    public void testGetCommand() {
        BotCommand expectedCommand = BotCommand.DOG_TRAINER_ADVICE;
        BotCommand actualCommand = dogTrainerAdviceHandler.getCommand();
        assertEquals(expectedCommand, actualCommand);
    }
}
