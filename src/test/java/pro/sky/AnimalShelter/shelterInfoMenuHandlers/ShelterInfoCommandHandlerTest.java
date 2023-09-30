package pro.sky.AnimalShelter.shelterInfoMenuHandlers;

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
import pro.sky.AnimalShelter.handlers.shelterInfoMenuHandlers.ShelterInfoCommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.*;

@ExtendWith(MockitoExtension.class)
class ShelterInfoCommandHandlerTest {

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
    private ShelterInfoCommandHandler shelterInfoCommandHandler;

    @BeforeEach
    public void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверяет, что при выполнении команды /shelter_info, если текущее состояние чата " +
            "(chatId) равно /shelter_info, и предыдущее состояние чата равно " +
            "/DOG, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleDogStateSendCorrectMessage() {
        Long chatId = 123L;
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        BotCommand previousState = chatStateService.getLastStateCatOrDogByChatId(chatId);
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(SHELTER_INFO);
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(DOG);
        shelterInfoCommandHandler.handle(update);
        String s = currentState == SHELTER_INFO ? "Вы уже в этом меню." : "";
        String shelterType = currentState == DOG ? "приюте для собак" : currentState == SHELTER_INFO
                ? previousState == DOG ? "приюте для собак" : "приюте для кошек" : "приюте для кошек";
        String responseText = s + "Какую информацию вы бы хотели получить о " + shelterType + ":\n" +
                "1. Описание приюта (/description)\n" +
                "2. Расписание работы и контакты (/schedule)\n" +
                "3. Контактные данные охраны для пропуска (/pass)\n" +
                "4. Техника безопасности на территории приюта (/safety)\n" +
                "5. Оставить контактные данные (/contact)\n" +
                "6. Позвать волонтера (/help)\n" +
                "7. Назад (/back)\n" +
                "8. Выключить бота (/stop)";
        SendMessage message = new SendMessage(chatId.toString(), responseText);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }

    @Test
    @DisplayName("Проверяет, что при выполнении команды /shelter_info, если текущее состояние чата " +
            "(chatId) равно /shelter_info, и предыдущее состояние чата равно " +
            "/cat, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleCatStateSendCorrectMessage() {
        Long chatId = 123L;
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        BotCommand previousState = chatStateService.getLastStateCatOrDogByChatId(chatId);
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(SHELTER_INFO);
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(CAT);
        shelterInfoCommandHandler.handle(update);
        String s = currentState == SHELTER_INFO ? "Вы уже в этом меню." : "";
        String shelterType = currentState == CAT ? "приюте для собак" : currentState == SHELTER_INFO
                ? previousState == CAT ? "приюте для собак" : "приюте для кошек" : "приюте для кошек";
        String responseText = s + "Какую информацию вы бы хотели получить о " + shelterType + ":\n" +
                "1. Описание приюта (/description)\n" +
                "2. Расписание работы и контакты (/schedule)\n" +
                "3. Контактные данные охраны для пропуска (/pass)\n" +
                "4. Техника безопасности на территории приюта (/safety)\n" +
                "5. Оставить контактные данные (/contact)\n" +
                "6. Позвать волонтера (/help)\n" +
                "7. Назад (/back)\n" +
                "8. Выключить бота (/stop)";
        SendMessage message = new SendMessage(chatId.toString(), responseText);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }


    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса ShelterInfoCommandHandler " +
            "в состоянии \"Старт\" отправляется сообщение с текстом " +
            "в чат с заданным chatId.")
    public void testDescriptionCommandHandlerDog() {
        Long chatId = 123L;
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        BotCommand previousState = chatStateService.getLastStateCatOrDogByChatId(chatId);
        String s = currentState == SHELTER_INFO ? "Вы уже в этом меню." : "";
        String shelterType = currentState == CAT ? "приюте для собак" : currentState == SHELTER_INFO
                ? previousState == CAT ? "приюте для собак" : "приюте для кошек" : "приюте для кошек";
        String responseText = s + "Какую информацию вы бы хотели получить о " + shelterType + ":\n" +
                "1. Описание приюта (/description)\n" +
                "2. Расписание работы и контакты (/schedule)\n" +
                "3. Контактные данные охраны для пропуска (/pass)\n" +
                "4. Техника безопасности на территории приюта (/safety)\n" +
                "5. Оставить контактные данные (/contact)\n" +
                "6. Позвать волонтера (/help)\n" +
                "7. Назад (/back)\n" +
                "8. Выключить бота (/stop)";
        SendMessage message = new SendMessage(chatId.toString(), responseText);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }


    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса ShelterInfoCommandHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        shelterInfoCommandHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса ShelterInfoCommandHandler \" +\n"
            + "в состоянии \\\"Назад\\\" вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        shelterInfoCommandHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса ShelterInfoCommandHandler возвращает правильную команду BotCommand.DESCRIPTION")
    public void testGetCommand() {
        BotCommand actualCommand = shelterInfoCommandHandler.getCommand();
        assertEquals(SHELTER_INFO, actualCommand);
    }
}
