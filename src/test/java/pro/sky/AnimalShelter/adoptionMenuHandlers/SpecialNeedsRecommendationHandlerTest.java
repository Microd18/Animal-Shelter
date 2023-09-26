package pro.sky.AnimalShelter.adoptionMenuHandlers;

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
import pro.sky.AnimalShelter.handlers.adoptionMenuHandlers.SpecialNeedsRecommendationHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.SPECIAL_NEEDS_RECOMMENDATION_CAT_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.SPECIAL_NEEDS_RECOMMENDATION_DOG_TEXT;

@ExtendWith(MockitoExtension.class)
public class SpecialNeedsRecommendationHandlerTest {

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
    private SpecialNeedsRecommendationHandler specialNeedsRecommendationHandler;

    @BeforeEach
    public void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса SpecialNeedsRecommendationHandler " +
            "в состоянии \"Рекомендации по обустройству дома для животного с ограниченными возможностями\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testSpecialNeedsRecommendationHandlerDog() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, SPECIAL_NEEDS_RECOMMENDATION_DOG_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса SpecialNeedsRecommendationHandler " +
            "в состоянии \"Рекомендации по обустройству дома для животного с ограниченными возможностями\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testSpecialNeedsRecommendationHandlerCat() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, SPECIAL_NEEDS_RECOMMENDATION_CAT_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса SpecialNeedsRecommendationHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        specialNeedsRecommendationHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса SpecialNeedsRecommendationHandler \" +\n"
            + "в состоянии \\\"Назад\\\" вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        specialNeedsRecommendationHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса SpecialNeedsRecommendationHandler возвращает правильную команду BotCommand.SPECIAL_NEED_RECOMMENDATION")
    public void testGetCommand() {
        BotCommand expectedCommand = BotCommand.SPECIAL_NEED_RECOMMENDATION;
        BotCommand actualCommand = specialNeedsRecommendationHandler.getCommand();
        assertEquals(expectedCommand, actualCommand);
    }
}
