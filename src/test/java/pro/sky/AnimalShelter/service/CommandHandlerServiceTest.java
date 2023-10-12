package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.handlers.generalHandlers.StartCommandHandler;
import pro.sky.AnimalShelter.utils.ValidationUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.enums.CheckUserReportStates.ALL_REPORTS;
import static pro.sky.AnimalShelter.enums.UserReportStates.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.ADMIN_COMMAND_TEXT;

@ExtendWith(MockitoExtension.class)
class CommandHandlerServiceTest {

    @Mock
    private VolunteerService volunteerService;

    @Mock
    private UserService userService;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private ChatStateService chatStateService;

    @Mock
    private UserReportStateService userReportStateService;

    @Mock
    private CheckUserReportStateService checkUserReportStateService;

    @Mock
    private CheckUserReportService checkUserReportService;

    @Mock
    private UserReportService userReportService;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @Mock
    private List<CommandHandler> commandHandlers;

    private CommandHandler matchedHandler;

    private ArgumentCaptor<SendMessage> captor;

    private Long chatId;

    private String messageText;

    @InjectMocks
    private CommandHandlerService commandHandlerService;

    @BeforeEach
    void setUp() {
        chatId = 123L;
        messageText = "/start";
        captor = ArgumentCaptor.forClass(SendMessage.class);
        matchedHandler = mock(StartCommandHandler.class);
    }

    @Test
    @DisplayName("Проверка на обработку входящего обновления, когда message равно null")
    void testProcessHandleMessageNull() {
        when(update.message()).thenReturn(null);

        commandHandlerService.process(update);

        verifyNoInteractions(telegramBot, chatStateService,
                userReportStateService, volunteerService, userService,
                validationUtils, userReportService, checkUserReportService, checkUserReportStateService);

    }

    @Test
    @DisplayName("Проверка на обработку входящего обновления, когда messageText равно null, currentState == SEND_REPORT, reportCurrentState == PHOTO, и есть фото")
    void testProcessHandleMessageTextNullWithPhoto() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.text()).thenReturn(null);
        when(chat.id()).thenReturn(chatId);
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(SEND_REPORT);
        when(userReportStateService.getCurrentStateByChatId(chatId)).thenReturn(PHOTO);
        when(message.photo()).thenReturn(new PhotoSize[]{mock(PhotoSize.class)});

        commandHandlerService.process(update);

        verify(userReportService, times(1)).savePhotoForReport(anyLong(), any(PhotoSize[].class));
        verify(checkUserReportStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(userReportStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(chatStateService, times(1)).getCurrentStateByChatId(chatId);
    }

    @Test
    @DisplayName("Проверка на обработку входящего обновления,когда команда неизвестна")
    void testProcessHandleUnknownCommand() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.text()).thenReturn(null);
        when(chat.id()).thenReturn(chatId);
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(SHELTER_INFO);
        when(userReportStateService.getCurrentStateByChatId(chatId)).thenReturn(BEHAVIOR);

        commandHandlerService.process(update);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Неизвестная команда", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));

        verify(userReportService, times(0)).savePhotoForReport(anyLong(), any(PhotoSize[].class));
        verify(checkUserReportStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(userReportStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(chatStateService, times(1)).getCurrentStateByChatId(chatId);
    }

    @Test
    @DisplayName("Проверка обработки входящего обновления с командой, которая нашлась в списке обработчиков")
    void testProcessCommandFound() throws NoSuchFieldException, IllegalAccessException {
        Field field = CommandHandlerService.class.getDeclaredField("commandHandlers");
        field.setAccessible(true);
        commandHandlers.add(matchedHandler);
        field.set(commandHandlerService, commandHandlers);

        when(matchedHandler.getCommand()).thenReturn(START);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.text()).thenReturn(messageText);
        when(chat.id()).thenReturn(chatId);
        when(commandHandlers.stream()).thenReturn(Stream.of(matchedHandler));

        commandHandlerService.process(update);

        verify(matchedHandler).handle(update);

        verify(userReportService, times(0)).savePhotoForReport(anyLong(), any(PhotoSize[].class));
        verify(checkUserReportStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(userReportStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(chatStateService, times(1)).getCurrentStateByChatId(chatId);
    }

    @Test
    @DisplayName("Проверка обработки входящего обновления, когда состояние проверки пароля админа и пароль валидный")
    void testProcessWhenCurrentStateCHECK_ADMIN_PASSWORDValidPassword() throws NoSuchFieldException, IllegalAccessException {
        Field field = CommandHandlerService.class.getDeclaredField("commandHandlers");
        field.setAccessible(true);
        commandHandlers.add(matchedHandler);
        field.set(commandHandlerService, commandHandlers);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.text()).thenReturn(messageText);
        when(chat.id()).thenReturn(chatId);
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(CHECK_ADMIN_PASSWORD);
        when(commandHandlers.stream()).thenReturn(Stream.empty());
        when(validationUtils.isValidAdminPassword(anyString())).thenReturn(true);

        commandHandlerService.process(update);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(ADMIN_COMMAND_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));

        verify(userReportService, times(0)).savePhotoForReport(anyLong(), any(PhotoSize[].class));
        verify(checkUserReportStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(userReportStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(chatStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(chatStateService, times(1)).updateChatState(chatId, ADMIN);
    }

    @Test
    @DisplayName("Проверка обработки входящего обновления, когда состояние проверки пароля админа и пароль невалидный")
    void testProcessWhenCurrentStateCHECK_ADMIN_PASSWORDInvalidPassword() throws NoSuchFieldException, IllegalAccessException {
        Field field = CommandHandlerService.class.getDeclaredField("commandHandlers");
        field.setAccessible(true);
        commandHandlers.add(matchedHandler);
        field.set(commandHandlerService, commandHandlers);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.text()).thenReturn(messageText);
        when(chat.id()).thenReturn(chatId);
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(CHECK_ADMIN_PASSWORD);
        when(commandHandlers.stream()).thenReturn(Stream.empty());
        when(validationUtils.isValidAdminPassword(anyString())).thenReturn(false);

        commandHandlerService.process(update);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Введён неправильный пароль", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));

        verify(userReportService, times(0)).savePhotoForReport(anyLong(), any(PhotoSize[].class));
        verify(checkUserReportStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(userReportStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(chatStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(chatStateService, times(0)).updateChatState(chatId, ADMIN);
    }

    @ParameterizedTest
    @DisplayName("Проверка обработки входящего обновления, когда состояния равны: " +
            "EXTENSION_PROBATION, SUCCESSFUL_PROBATIONARY, PROBATION_FAILED," +
            "CONTACT, FIND_USER_BY_PHONE, FIND_ANIMAL_BY_NAME, MAKE_ADOPTER, CHECK_REPORT, SEND_REPORT")
    @EnumSource(value = BotCommand.class, names = {"EXTENSION_PROBATION", "SUCCESSFUL_PROBATIONARY", "PROBATION_FAILED",
            "CONTACT", "FIND_USER_BY_PHONE", "FIND_ANIMAL_BY_NAME", "MAKE_ADOPTER", "CHECK_REPORT", "SEND_REPORT"})
    void testProcessWithDifferentBotCommands(BotCommand currentState) throws NoSuchFieldException, IllegalAccessException {
        Field field = CommandHandlerService.class.getDeclaredField("commandHandlers");
        field.setAccessible(true);
        commandHandlers.add(matchedHandler);
        field.set(commandHandlerService, commandHandlers);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.text()).thenReturn(messageText);
        when(chat.id()).thenReturn(chatId);
        lenient().when(checkUserReportStateService.getCurrentStateByChatId(chatId)).thenReturn(ALL_REPORTS);
        lenient().when(userReportStateService.getCurrentStateByChatId(chatId)).thenReturn(RATION);
        when(chatStateService.getCurrentStateByChatId(chatId)).thenReturn(currentState);
        when(commandHandlers.stream()).thenReturn(Stream.empty());

        commandHandlerService.process(update);

        if (currentState == EXTENSION_PROBATION) {
            verify(volunteerService, times(1)).increaseProbationPeriod(chatId, messageText);
        } else if (currentState == SUCCESSFUL_PROBATIONARY) {
            verify(volunteerService, times(1)).giveAnimalAway(chatId, messageText);
        } else if (currentState == PROBATION_FAILED) {
            verify(volunteerService, times(1)).takeBackAnimal(chatId, messageText);
        } else if (currentState == CONTACT) {
            verify(userService, times(1)).updateContact(chatId, messageText);
        } else if (currentState == FIND_USER_BY_PHONE) {
            verify(volunteerService, times(1)).findUsersByPhone(chatId, messageText);
        } else if (currentState == FIND_ANIMAL_BY_NAME) {
            verify(volunteerService, times(1)).findAnimalByName(chatId, messageText);
        } else if (currentState == MAKE_ADOPTER) {
            verify(volunteerService, times(1)).allowUserBecomeAdopter(chatId, messageText);
        } else if (currentState == CHECK_REPORT) {
            verify(checkUserReportService, times(1)).determinateAndSetCheckReportState(chatId, messageText, ALL_REPORTS);
        } else if (currentState == SEND_REPORT) {
            verify(userReportService, times(1)).saveReportData(chatId, messageText, RATION);
        }

        verify(userReportService, times(0)).savePhotoForReport(anyLong(), any(PhotoSize[].class));
        verify(checkUserReportStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(userReportStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(chatStateService, times(1)).getCurrentStateByChatId(chatId);
        verify(chatStateService, times(0)).updateChatState(chatId, BotCommand.ADMIN);
        verifyNoInteractions(validationUtils);
    }
}