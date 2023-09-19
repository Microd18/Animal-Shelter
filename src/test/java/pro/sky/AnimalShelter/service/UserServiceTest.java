package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.entity.User;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.UserRepository;
import pro.sky.AnimalShelter.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private UserService userService;

    private Long chatId;
    private String validMessageText;
    private String invalidMessageText;
    private String invalidName;
    private String validName;
    private String validPhoneNumber;
    private String validEmail;
    private List<String> invalidFields;

    @BeforeEach
    public void setUp() {
        chatId = 123L;
        validMessageText = "name,89997776655,name@example.com";
        invalidMessageText = "Invalid Data";
        invalidName = "12345";
        validName = "name";
        validPhoneNumber = "89997776655";
        validEmail = "name@example.com";
        invalidFields = List.of("Имя", "Телефон");

        lenient().when(chatRepository.findByChatId(chatId)).thenReturn(Optional.of(new Chat()));
        lenient().when(validationUtils.isValidEmail(validEmail)).thenReturn(true);
        lenient().when(validationUtils.isValidPhoneNumber(validPhoneNumber)).thenReturn(true);
    }

    @Test
    @DisplayName("Проверка на валидные данные контакта")
    public void testUpdateContactValidInput() {

        when(userRepository.findByChatId(any())).thenReturn(Optional.empty());
        when(validationUtils.isValidName(validName)).thenReturn(true);

        userService.updateContact(chatId, validMessageText);

        verify(userRepository).save(any());
        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка на невалидные данные контакта")
    public void testUpdateContactInvalidInput() {

        userService.updateContact(chatId, invalidMessageText);

        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка на невалидное имя контакта")
    public void testUpdateContactInvalidName() {

        String messageText = "12345,89997776655,name@example.com";

        lenient().when(userRepository.findByChatId(any())).thenReturn(Optional.empty());
        when(validationUtils.isValidName(invalidName)).thenReturn(false);

        userService.updateContact(chatId, messageText);

        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка на ошибку в базе данных при обновлении контакта")
    public void testUpdateContactDatabaseError() {

        when(chatRepository.findByChatId(chatId)).thenReturn(Optional.of(new Chat()));
        when(userRepository.findByChatId(any())).thenReturn(Optional.empty());
        when(validationUtils.isValidName(validName)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(userRepository).save(any());

        assertThrows(RuntimeException.class, () -> userService.updateContact(chatId, validMessageText));

        verify(telegramBot, times(0)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка на существующего пользователя")
    public void testUpdateContactUserExists() {

        when(userRepository.findByChatId(any())).thenReturn(Optional.of(new User()));
        when(validationUtils.isValidName(validName)).thenReturn(true);

        userService.updateContact(chatId, validMessageText);

        verify(userRepository).save(any());
        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка отправки сообщения с ошибками валидации")
    public void testSendValidationErrors() {

        userService.sendValidationErrors(chatId, invalidFields);

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка обновления данных пользователя (пользователь существует)")
    public void testUpdateUserDataUserExists() {

        when(userRepository.findByChatId(any())).thenReturn(Optional.of(new User()));

        userService.updateUserData(chatId, validName, validPhoneNumber, validEmail);

        verify(userRepository).save(any());
        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка обновления данных пользователя (пользователь не существует)")
    public void testUpdateUserDataUserDoesNotExist() {

        when(userRepository.findByChatId(any())).thenReturn(Optional.empty());

        userService.updateUserData(chatId, validName, validPhoneNumber, validEmail);

        verify(userRepository).save(any());
        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка отправки сообщения об ошибке ввода")
    public void testSendInvalidInputMessage() {

        userService.sendInvalidInputMessage(chatId);

        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка валидности данных контакта (валидное имя)")
    public void testIsValidContactDataValidName() {
        int index = 0;

        when(validationUtils.isValidName(validName)).thenReturn(true);

        boolean result = userService.isValidContactData(validName, index);

        assertTrue(result);
    }

    @Test
    @DisplayName("Проверка валидности данных контакта (невалидное имя)")
    public void testIsValidContactDataInvalidName() {
        int index = 0;

        when(validationUtils.isValidName(invalidName)).thenReturn(false);

        boolean result = userService.isValidContactData(invalidName, index);

        assertFalse(result);
    }

    @Test
    @DisplayName("Проверка на невалидный номер телефона в данных контакта")
    public void testUpdateContactInvalidPhoneNumber() {

        String messageText = "name,invalidPhone,name@example.com";

        when(validationUtils.isValidName(validName)).thenReturn(true);
        when(validationUtils.isValidPhoneNumber("invalidPhone")).thenReturn(false);
        when(validationUtils.isValidEmail(validEmail)).thenReturn(true);

        userService.updateContact(chatId, messageText);

        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка на невалидный email в данных контакта")
    public void testUpdateContactInvalidEmail() {

        String messageText = "name,89997776655,invalidEmail";

        when(validationUtils.isValidName(validName)).thenReturn(true);
        when(validationUtils.isValidPhoneNumber(validPhoneNumber)).thenReturn(true);
        when(validationUtils.isValidEmail("invalidEmail")).thenReturn(false);

        userService.updateContact(chatId, messageText);

        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка на существующего пользователя с невалидным вводом")
    public void testUpdateContactUserExistsInvalidInput() {

        userService.updateContact(chatId, invalidMessageText);

        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка на существующего пользователя с валидным вводом")
    public void testUpdateContactUserExistsValidInput() {

        when(userRepository.findByChatId(any())).thenReturn(Optional.of(new User()));
        when(validationUtils.isValidName(validName)).thenReturn(true);

        userService.updateContact(chatId, validMessageText);

        verify(userRepository).save(any());
        verify(telegramBot).execute(any(SendMessage.class));
    }
}
