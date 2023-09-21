package pro.sky.AnimalShelter.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

/**
 * Утилитарный класс для выполнения различных видов валидации.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationUtils {

    /**
     * Проверяет, является ли переданное имя допустимым именем.
     *
     * @param name Имя для проверки.
     * @return true, если имя допустимо, в противном случае - false.
     */
    public Boolean isValidName(String name) {

        if (StringUtils.isBlank(name)) {
            return false;
        }
        return StringUtils.isAlphaSpace(name);
    }

    /**
     * Проверяет, является ли переданный номер телефона допустимым номером в формате России.
     *
     * @param phoneNumber Номер телефона для проверки.
     * @return true, если номер телефона допустим, в противном случае - false.
     */
    public Boolean isValidPhoneNumber(String phoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, "RU");

            return phoneNumberUtil.isValidNumber(parsedNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }

    /**
     * Форматирует переданный номер телефона в стандартный формат E.164.
     *
     * @param phoneNumber Номер телефона для форматирования.
     * @return Строка, представляющая отформатированный номер телефона в формате E.164.
     */
    public String phoneNumberFormat(String phoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, "RU");
            return phoneNumberUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            return phoneNumber;
        }
    }

    /**
     * Проверяет, является ли переданный адрес электронной почты допустимым.
     *
     * @param email Адрес электронной почты для проверки.
     * @return true, если адрес электронной почты допустим, в противном случае - false.
     */
    public boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}
