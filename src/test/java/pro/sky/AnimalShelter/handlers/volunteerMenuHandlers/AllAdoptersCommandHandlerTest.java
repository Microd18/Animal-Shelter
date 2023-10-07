package pro.sky.AnimalShelter.handlers.volunteerMenuHandlers;

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
import pro.sky.AnimalShelter.service.VolunteerService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.ADMIN;
import static pro.sky.AnimalShelter.enums.BotCommand.ALL_ADOPTERS;
import static pro.sky.AnimalShelter.utils.MessagesBot.WAITING_ANIMAL_NAME_TEXT;

@ExtendWith(MockitoExtension.class)
class AllAdoptersCommandHandlerTest {
    @Mock
    private ChatStateService chatStateService;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private VolunteerService volunteerService;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @InjectMocks
    private AllAdoptersCommandHandler allAdoptersCommandHandler;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Тестируем случай, когда currentState == ADMIN")
    public void testHandleStateSendCorrectMessage() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(ADMIN);
        allAdoptersCommandHandler.handle(update);
        verify(volunteerService, times(1)).getAllAdopters(chatId);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса AllAdoptersCommandHandler " +
            "в состоянии \"Меню Admin\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testFindAnimalByNameCommandHandler() {
        Long chatId = 123L;
        SendMessage message = new SendMessage(chatId, WAITING_ANIMAL_NAME_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса AllAdoptersCommandHandler \" +\n"
            + "в состоянии \\\"Назад\\\" вызывается метод execute класса TelegramBot.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);
        allAdoptersCommandHandler.handle(update);
        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса AllAdoptersCommandHandler возвращает правильную команду /all_adopters")
    public void testGetCommand() {
        BotCommand actualCommand = allAdoptersCommandHandler.getCommand();
        assertEquals(ALL_ADOPTERS, actualCommand);
    }
}