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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.*;

@ExtendWith(MockitoExtension.class)
class SuccessfulProbationaryCommandHandlerTest {
    @Mock
    private ChatStateService chatStateService;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @InjectMocks
    private SuccessfulProbationaryCommandHandler successfulProbationaryCommandHandler;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, что при выполнении команды /find_animal_by_name, если текущее состояние чата " +
            "(chatId) равно /ADOPT ,, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleStateSendCorrectMessage() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(ADMIN);
        successfulProbationaryCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId, "Введите ID юзера,кошка(или собака)");
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Тест на проверку отправки сообщения \"Сперва зайдите в меню волонтёра\", если текущее состояние чата не является ADMIN.")
    public void testHandleNotAdmin() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(START);
        successfulProbationaryCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId, "Сперва зайдите в меню волонтёра");
        telegramBot.execute(message);
        verify(telegramBot).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса FindAnimalByNameCommandHandler возвращает правильную команду /probation_failed")
    public void testGetCommand() {
        BotCommand actualCommand = successfulProbationaryCommandHandler.getCommand();
        assertEquals(SUCCESSFUL_PROBATIONARY, actualCommand);
    }


}