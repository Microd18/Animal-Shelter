package pro.sky.AnimalShelter.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ValidationUtilsTest {

    private final ValidationUtils validationUtils = new ValidationUtils();

    @Test
    @DisplayName("Проверка на валидное имя")
    void shouldReturnTrueForValidName() {
        String validName = "Dima Volkov";
        boolean result = validationUtils.isValidName(validName);
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Проверка на невалидное имя")
    void shouldReturnFalseForInvalidName() {
        String invalidName = "Dima23";
        boolean result = validationUtils.isValidName(invalidName);
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Проверка на пустое заполнение")
    void shouldReturnFalseForBlankName() {
        String blankName = "   ";
        boolean result = validationUtils.isValidName(blankName);
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Проверка на валидный номер")
    void shouldReturnTrueForValidPhoneNumber() {
        String validPhoneNumber = "+79000000000";
        boolean result = validationUtils.isValidPhoneNumber(validPhoneNumber);
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Проверка на невалидный номер")
    void shouldReturnFalseForInvalidPhoneNumber() {
        String invalidPhoneNumber = "+790000000000";
        boolean result = validationUtils.isValidPhoneNumber(invalidPhoneNumber);
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Проверка на валидный mail")
    void shouldReturnTrueForValidEmail() {
        String validEmail = "test@example.com";
        boolean result = validationUtils.isValidEmail(validEmail);
        Assertions.assertTrue(result);

    }

    @Test
    @DisplayName("Проверка на невалидный mail")
    void shouldReturnFalseForInvalidEmail() {
        String invalidEmail = "testexample.com";
        boolean result = validationUtils.isValidEmail(invalidEmail);
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Проверка на формат правильного ввода номера")
    void shouldReturnFormattedPhoneNumber() {
        String phoneNumber = "+7 (900) 000-00-00";
        String formattedPhoneNumber = validationUtils.phoneNumberFormat(phoneNumber);
        Assertions.assertEquals("+79000000000", formattedPhoneNumber);
    }

    @Test
    @DisplayName("Проверка на правильный ввод номера")
    void shouldReturnInvalidPhoneNumberForWrongFormat() {
        String formattedPhoneNumber = validationUtils.phoneNumberFormat("+712345");
        Assertions.assertEquals("+712345", formattedPhoneNumber);
    }

    @Test
    @DisplayName("Проверка на правильное имя")
    void shouldReturnValidNameForNameWithHyphen() {
        String nameWithHyphen = "Dima Volkov";
        boolean result = validationUtils.isValidName(nameWithHyphen);
        Assertions.assertTrue(result);
    }
}