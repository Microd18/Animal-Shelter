package pro.sky.AnimalShelter.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidationUtilsTest {

    private final ValidationUtils validationUtils = new ValidationUtils();

    @Test
    void shouldReturnTrueForValidName() {
        String validName = "Dima Volkov";
        boolean result = validationUtils.isValidName(validName);
        Assertions.assertTrue(result);
    }

    @Test
    void shouldReturnFalseForInvalidName() {
        String invalidName = "Dima23";
        boolean result = validationUtils.isValidName(invalidName);
        Assertions.assertFalse(result);
    }

    @Test
    void shouldReturnFalseForBlankName() {
        String blankName = "   ";
        boolean result = validationUtils.isValidName(blankName);
        Assertions.assertFalse(result);
    }

    @Test
    void shouldReturnTrueForValidPhoneNumber() {
        String validPhoneNumber = "+79000000000";
        boolean result = validationUtils.isValidPhoneNumber(validPhoneNumber);
        Assertions.assertTrue(result);
    }

    @Test
    void shouldReturnFalseForInvalidPhoneNumber() {
        String invalidPhoneNumber = "+790000000000"; // should contain 11 digits
        boolean result = validationUtils.isValidPhoneNumber(invalidPhoneNumber);
        Assertions.assertFalse(result);
    }

    @Test
    void shouldReturnTrueForValidEmail() {
        String validEmail = "test@example.com";
        boolean result = validationUtils.isValidEmail(validEmail);
        Assertions.assertTrue(result);

    }

    @Test
    void shouldReturnFalseForInvalidEmail() {
        String invalidEmail = "testexample.com";
        boolean result = validationUtils.isValidEmail(invalidEmail);
        Assertions.assertFalse(result);
    }

    @Test
    void shouldReturnFormattedPhoneNumber() {
        String phoneNumber = "+7 (900) 000-00-00";
        String formattedPhoneNumber = validationUtils.phoneNumberFormat(phoneNumber);
        Assertions.assertEquals("+79000000000", formattedPhoneNumber);
    }

    @Test
    void shouldReturnInvalidPhoneNumberForWrongFormat() {
        String formattedPhoneNumber = validationUtils.phoneNumberFormat("+712345");
        Assertions.assertEquals("+712345", formattedPhoneNumber);
    }

    @Test
    void shouldReturnValidNameForNameWithHyphen() {
        String nameWithHyphen = "Dima Volkov";
        boolean result = validationUtils.isValidName(nameWithHyphen);
        Assertions.assertTrue(result);
    }
}