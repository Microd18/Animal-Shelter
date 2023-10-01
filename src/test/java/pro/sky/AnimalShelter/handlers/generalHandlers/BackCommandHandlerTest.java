package pro.sky.AnimalShelter.handlers.generalHandlers;

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
import pro.sky.AnimalShelter.service.ChatService;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.service.UserReportStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.ADMIN_COMMAND_RETURN_TEXT;

@ExtendWith(MockitoExtension.class)
class BackCommandHandlerTest {
    @Mock
    private ChatStateService chatStateService;

    @Mock
    private UserReportStateService userReportStateService;

    @Mock
    private ChatService chatService;

    @Mock
    private CommonUtils commonUtils;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @InjectMocks
    private BackCommandHandler backCommandHandler;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Тестирование обработки команды /back в состоянии SEND_REPORT:")
    public void testHandleBackCommandSendReportStateReturnToPreviousMenu() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(SEND_REPORT);
        when(chatStateService.getLastStateCatOrDogByChatId(chatId)).thenReturn(DOG);
        backCommandHandler.handle(update);
        String responseText = "Вы вернулись назад. Это меню для отправки отчёта, выберите действие:\n" +
                "1. Отправить отчёт: выберите этот пункт меню, чтобы отправить ежедневный отчёт о вашем питомце (/send_report)\n" +
                "2. Посмотреть шаблон для отчёта: если вам нужно ознакомиться с шаблоном для ежедневного отчёта перед его отправкой, выберите этот пункт меню (/report_template)\n" +
                "3. Назад* (/back)\n" +
                "4. Выключить бота (/stop)";
        telegramBot.execute(new SendMessage(chatId, responseText));
        verify(userReportStateService).clearUserReportStates(chatId);
        verify(chatStateService).goToPreviousState(chatId);
    }

    @Test
    @DisplayName("Тестирование обработки команды /back в состоянии CONTACT:")
    public void testHandleBackCommandContactStateReturnToPreviousMenu() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(CONTACT);
        when(chatStateService.getLastStateCatOrDogByChatId(chatId)).thenReturn(DOG);
        backCommandHandler.handle(update);
        BotCommand lastState = chatStateService.getLastStateCatOrDogByChatId(chatId);
        String shelterType = lastState == DOG ? "приюте для собак" : "приюте для кошек";
        String responseText = "Вы вернулись назад. Какую информацию вы бы хотели получить о " + shelterType + ":\n" +
                "1. Описание приюта (/description)\n" +
                "2. Расписание работы и контакты (/schedule)\n" +
                "3. Контактные данные охраны для пропуска (/pass)\n" +
                "4. Техника безопасности на территории приюта (/safety)\n" +
                "5. Оставить контактные данные (/contact)\n" +
                "6. Позвать волонтера (/help)\n" +
                "7. Назад (/back)\n" +
                "8. Выключить бота (/stop)";

        telegramBot.execute(new SendMessage(chatId, responseText));
        verify(chatStateService).goToPreviousState(chatId);
    }

    @Test
    @DisplayName("Тестирование обработки команды /back в состоянии FIND_USER_BY_PHONE:")
    public void testHandleBackCommandFindUserByPhoneStateReturnToPreviousMenu() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(FIND_USER_BY_PHONE);
        backCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId.toString(), ADMIN_COMMAND_RETURN_TEXT);
        telegramBot.execute(message);
        verify(chatStateService).goToPreviousState(chatId);
    }

    @Test
    @DisplayName("Тестирование обработки команды /back в состоянии FIND_ANIMAL_BY_NAME:")
    public void testHandleBackCommandFindAnimalByNameStateReturnToPreviousMenu() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(FIND_ANIMAL_BY_NAME);
        backCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId.toString(), ADMIN_COMMAND_RETURN_TEXT);
        telegramBot.execute(message);
        verify(chatStateService).goToPreviousState(chatId);
    }

    @Test
    @DisplayName("Тестирование обработки команды /back в состоянии MAKE_ADOPTER:")
    public void testHandleBackCommandMakeAdopterStateReturnToPreviousMenu() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(MAKE_ADOPTER);
        backCommandHandler.handle(update);
        SendMessage message = new SendMessage(chatId.toString(), ADMIN_COMMAND_RETURN_TEXT);
        telegramBot.execute(message);
        verify(chatStateService).goToPreviousState(chatId);
    }

    @Test
    @DisplayName("Тестирование обработки команды /back в состоянии SHELTER_INFO или ADOPT или PET_REPORT  :")
    public void testHandleBackCommandShalterInfoStateReturnToPreviousMenu() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(SHELTER_INFO);
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(ADOPT);
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(PET_REPORT);
        backCommandHandler.handle(update);
        BotCommand previousState = chatStateService.getLastStateCatOrDogByChatId(chatId);
        var shelterType = previousState == CAT ? "приют для кошек" : "приют для собак";
        String responseText = "Вы вернулись назад. У вас выбран " + shelterType + ". Чем я могу помочь?\n" +
                "1. Узнать информацию о приюте (/shelter_info)\n" +
                "2. Как взять животное из приюта (/adopt)\n" +
                "3. Прислать отчет о питомце (/pet_report)\n" +
                "4. Позвать волонтера (/help)\n" +
                "5. Назад (/back)\n" +
                "6. Выключить бота (/stop)";
        telegramBot.execute(new SendMessage(chatId.toString(), responseText));
        verify(chatStateService).goToPreviousState(chatId);
    }

    @Test
    @DisplayName("Тестирование метода getCommand()")
    public void getCommand_returnsCorrectCommand() {
        BotCommand actualCommand = backCommandHandler.getCommand();
        assertEquals(BACK, actualCommand);

    }
}