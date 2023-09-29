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
import static pro.sky.AnimalShelter.utils.MessagesBot.HOME_SETUP_RECOMMENDATION_CAT_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.HOME_SETUP_RECOMMENDATION_DOG_TEXT;

@ExtendWith(MockitoExtension.class)
public class HomeSetupRecommendationHandlerTest {

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
    private HomeSetupRecommendationHandler homeSetupRecommendationHandler;

    @BeforeEach
    public void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, что при выполнении команды /home_setup_recommendations, если текущее состояние чата " +
            "(chatId) равно /ADOPT, и предыдущее состояние чата равно " +
            "/DOG, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleDogStateSendCorrectMessage() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.ADOPT);
        when(chatStateService.getPreviousStateByChatId(123L)).thenReturn(BotCommand.DOG);
        homeSetupRecommendationHandler.handle(update);
        SendMessage message = new SendMessage(chatId, HOME_SETUP_RECOMMENDATION_DOG_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при выполнении команды /home_setup_recommendations, если текущее состояние чата " +
            "(chatId) равно /ADOPT, и предыдущее состояние чата равно " +
            "/DOG, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleCatStateSendCorrectMessage() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.ADOPT);
        when(chatStateService.getPreviousStateByChatId(123L)).thenReturn(BotCommand.CAT);
        homeSetupRecommendationHandler.handle(update);
        SendMessage message = new SendMessage(chatId, HOME_SETUP_RECOMMENDATION_CAT_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса HomeSetupRecommendationHandler " +
            "в состоянии \"Рекомендации по обустройству дома для взрослого животного\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testHomeSetupRecommendationHandlerDog() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, HOME_SETUP_RECOMMENDATION_DOG_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса HomeSetupRecommendationHandler " +
            "в состоянии \"Рекомендации по обустройству дома для взрослого животного\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testHomeSetupRecommendationHandlerCat() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, HOME_SETUP_RECOMMENDATION_CAT_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса HomeSetupRecommendationHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        homeSetupRecommendationHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса HomeSetupRecommendationHandler \" +\n"
            + "в состоянии \\\"Назад\\\" вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        homeSetupRecommendationHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса HomeSetupRecommendationHandler возвращает правильную команду BotCommand.HOME_SETUP_RECOMMENDATIONS")
    public void testGetCommand() {
        BotCommand expectedCommand = BotCommand.HOME_SETUP_RECOMMENDATIONS;
        BotCommand actualCommand = homeSetupRecommendationHandler.getCommand();
        assertEquals(expectedCommand, actualCommand);
    }
}
