package pro.sky.AnimalShelter.shelterInfoMenuHandlers;

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
import pro.sky.AnimalShelter.handlers.shelterInfoMenuHandlers.DescriptionCommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static pro.sky.AnimalShelter.utils.MessagesBot.CAT_SHELTER_DESCRIPTION_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.DOG_SHELTER_DESCRIPTION_TEXT;

public class DescriptionCommandHandlerTest {
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

    private DescriptionCommandHandler descriptionCommandHandler;

    @Before
    public void setUp() {
        initMocks(this);
        descriptionCommandHandler = new DescriptionCommandHandler(chatStateService, telegramBot, commonUtils);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса DescriptionCommandHandler " +
            "в состоянии \"Описание приюта для собак\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testDescriptionCommandHandlerDog() {
        Long chatId = 123L;
        BotCommand currentState = BotCommand.DESCRIPTION;

        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(currentState);
        SendMessage message = new SendMessage(chatId.toString(), DOG_SHELTER_DESCRIPTION_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса DescriptionCommandHandler " +
            "в состоянии \"Описание приюта для кошек\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testDescriptionCommandHandlerCat() {
        Long chatId = 123L;
        BotCommand currentState = BotCommand.DESCRIPTION;

        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(currentState);
        SendMessage message = new SendMessage(chatId.toString(), CAT_SHELTER_DESCRIPTION_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса DescriptionCommandHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        descriptionCommandHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса DescriptionCommandHandler \" +\n"
            + "в состоянии \\\"Назад\\\" вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        descriptionCommandHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса DescriptionCommandHandler возвращает правильную команду BotCommand.DESCRIPTION")
    public void testGetCommand() {
        BotCommand expectedCommand = BotCommand.DESCRIPTION;
        BotCommand actualCommand = descriptionCommandHandler.getCommand();
        assertEquals(expectedCommand, actualCommand);
    }
}
