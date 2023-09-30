package pro.sky.AnimalShelter.handlers.shelterInfoMenuHandlers;

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
import pro.sky.AnimalShelter.handlers.shelterInfoMenuHandlers.DescriptionCommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.PASS_COMMAND_CAT_TEXT;

@ExtendWith(MockitoExtension.class)
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

    @InjectMocks
    private DescriptionCommandHandler descriptionCommandHandler;

    @BeforeEach
    public void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }
    @Test
    @DisplayName("Проверяет, что при выполнении команды /description, если текущее состояние чата " +
            "(chatId) равно /shelter_info, и предыдущее состояние чата равно " +
            "/DOG, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleDogStateSendCorrectMessage() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(SHELTER_INFO);
        when(chatStateService.getPreviousStateByChatId(123L)).thenReturn(DOG);
        descriptionCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId, DOG_SHELTER_DESCRIPTION_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }
    @Test
    @DisplayName("Проверяет, что при выполнении команды /pass, если текущее состояние чата " +
            "(chatId) равно /shelter_info, и предыдущее состояние чата равно " +
            "/CAT, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleCatStateSendCorrectMessage() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(SHELTER_INFO);
        when(chatStateService.getPreviousStateByChatId(123L)).thenReturn(CAT);
        descriptionCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId, DOG_SHELTER_DESCRIPTION_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса DescriptionCommandHandler " +
            "в состоянии \"Описание приюта для собак\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testDescriptionCommandHandlerDog() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, DOG_SHELTER_DESCRIPTION_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса DescriptionCommandHandler " +
            "в состоянии \"Описание приюта для кошек\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testDescriptionCommandHandlerCat() {
        Long chatId = 123L;

        SendMessage message = new SendMessage(chatId, CAT_SHELTER_DESCRIPTION_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
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
