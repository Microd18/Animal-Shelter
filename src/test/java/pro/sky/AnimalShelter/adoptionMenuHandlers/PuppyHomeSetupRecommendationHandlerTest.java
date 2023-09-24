package pro.sky.AnimalShelter.adoptionMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.adoptionMenuHandlers.PuppyHomeSetupRecommendationHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static pro.sky.AnimalShelter.utils.MessagesBot.PUPPY_HOME_SETUP_RECOMMENDATION_TEXT;

public class PuppyHomeSetupRecommendationHandlerTest {
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

    private PuppyHomeSetupRecommendationHandler puppyHomeSetupRecommendationHandler;

    @Before
    public void setUp() {
        initMocks(this);
        puppyHomeSetupRecommendationHandler = new PuppyHomeSetupRecommendationHandler(chatStateService, telegramBot, commonUtils);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса PuppyHomeSetupRecommendationHandler " +
            "в состоянии \"Рекомендации по обустройству дома для щенка\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testPuppyHomeSetupRecommendationHandler() {
        Long chatId = 123L;
        BotCommand currentState = BotCommand.PUPPY_HOME_SETUP_RECOMMENDATION;

        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(currentState);
        SendMessage message = new SendMessage(chatId.toString(), PUPPY_HOME_SETUP_RECOMMENDATION_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса PuppyHomeSetupRecommendationHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        puppyHomeSetupRecommendationHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса PuppyHomeSetupRecommendationHandler \" +\n"
            + "в состоянии \\\"Назад\\\" вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        puppyHomeSetupRecommendationHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса PuppyHomeSetupRecommendationHandler возвращает правильную команду BotCommand.PUPPY_HOME_SETUP_RECOMMENDATION")
    public void testGetCommand() {
        BotCommand expectedCommand = BotCommand.PUPPY_HOME_SETUP_RECOMMENDATION;
        BotCommand actualCommand = puppyHomeSetupRecommendationHandler.getCommand();
        assertEquals(expectedCommand, actualCommand);
    }
}

