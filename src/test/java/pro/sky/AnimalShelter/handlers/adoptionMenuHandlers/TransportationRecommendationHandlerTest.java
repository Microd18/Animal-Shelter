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
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.TRANSPORTATION_RECOMMENDATION_CAT_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.TRANSPORTATION_RECOMMENDATION_DOG_TEXT;

@ExtendWith(MockitoExtension.class)
public class TransportationRecommendationHandlerTest {

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
    private TransportationRecommendationHandler transportationRecommendationHandler;

    @BeforeEach
    public void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, ччто при выполнении команды /transportation_recommendation, если текущее состояние чата " +
            "(chatId) равно /ADOPT, и предыдущее состояние чата равно " +
            "/DOG, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleDogStateSendCorrectMessage() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.ADOPT);
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(BotCommand.DOG);
        transportationRecommendationHandler.handle(update);
        SendMessage message = new SendMessage(chatId, TRANSPORTATION_RECOMMENDATION_DOG_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при выполнении команды /transportation_recommendation, если текущее состояние чата " +
            "(chatId) равно /ADOPT, и предыдущее состояние чата равно " +
            "/CAT, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleCatStateSendCorrectMessage() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.ADOPT);
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(BotCommand.CAT);
        transportationRecommendationHandler.handle(update);
        SendMessage message = new SendMessage(chatId, TRANSPORTATION_RECOMMENDATION_CAT_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса TransportationRecommendationHandler " +
            "в состоянии \"Рекомендации по транспортировке собаки\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testTransportationRecommendationHandlerDog() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, TRANSPORTATION_RECOMMENDATION_DOG_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса TransportationRecommendationHandler " +
            "в состоянии \"Рекомендации по транспортировке кошки\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testTransportationRecommendationHandlerCat() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, TRANSPORTATION_RECOMMENDATION_CAT_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса TransportationRecommendationHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        transportationRecommendationHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса TransportationRecommendationHandler \" +\n"
            + "в состоянии \\\"Назад\\\" вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        transportationRecommendationHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса TransportationRecommendationHandler возвращает правильную команду BotCommand.TRANSPORTATION_RECOMMENDATION")
    public void testGetCommand() {
        BotCommand expectedCommand = BotCommand.TRANSPORTATION_RECOMMENDATION;
        BotCommand actualCommand = transportationRecommendationHandler.getCommand();
        assertEquals(expectedCommand, actualCommand);
    }
}
