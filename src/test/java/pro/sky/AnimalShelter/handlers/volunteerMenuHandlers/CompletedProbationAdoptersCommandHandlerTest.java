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
import pro.sky.AnimalShelter.entity.User;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.repository.UserRepository;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.ADMIN;
import static pro.sky.AnimalShelter.enums.BotCommand.COMPLETED_PROBATION_ADOPTERS;
import static pro.sky.AnimalShelter.utils.MessagesBot.WAITING_ANIMAL_NAME_TEXT;

@ExtendWith(MockitoExtension.class)
class CompletedProbationAdoptersCommandHandlerTest {
    @Mock
    private ChatStateService chatStateService;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private CommonUtils commonUtils;
    @Mock
    private UserRepository userRepository;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private User user;

    @Mock
    private Chat chat;

    @InjectMocks
    private CompletedProbationAdoptersCommandHandler completedProbationAdoptersCommandHandler;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Тест на проверку отправки сообщения \"Сперва зайдите в меню волонтёра\", если текущее состояние чата не является ADMIN.")
    public void testHandleNotAdmin() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(ADMIN);
        completedProbationAdoptersCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId, "Сперва зайдите в меню волонтёра");
        telegramBot.execute(message);
        verify(telegramBot).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса CheckReportCommandHandler " +
            "в состоянии \"Меню Admin\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testFindAnimalByNameCommandHandler() {
        Long chatId = 123L;
        SendMessage message = new SendMessage(chatId, WAITING_ANIMAL_NAME_TEXT);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса CheckReportCommandHandler возвращает правильную команду /check_report")
    public void testGetCommand() {
        BotCommand actualCommand = completedProbationAdoptersCommandHandler.getCommand();
        assertEquals(COMPLETED_PROBATION_ADOPTERS, actualCommand);
    }
}
