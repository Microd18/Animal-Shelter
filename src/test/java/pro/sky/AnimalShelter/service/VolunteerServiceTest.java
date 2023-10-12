package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.entity.Cat;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.entity.Dog;
import pro.sky.AnimalShelter.entity.User;
import pro.sky.AnimalShelter.entity.VolunteerInfoCat;
import pro.sky.AnimalShelter.entity.VolunteerInfoDog;
import pro.sky.AnimalShelter.repository.CatReportRepository;
import pro.sky.AnimalShelter.repository.CatRepository;
import pro.sky.AnimalShelter.repository.DogReportRepository;
import pro.sky.AnimalShelter.repository.DogRepository;
import pro.sky.AnimalShelter.repository.UserRepository;
import pro.sky.AnimalShelter.repository.VolunteerInfoCatRepository;
import pro.sky.AnimalShelter.repository.VolunteerInfoDogRepository;
import pro.sky.AnimalShelter.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static pro.sky.AnimalShelter.utils.MessagesBot.ADOPTERS_NOT_FOUND_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.ADOPTER_ALREADY_TOOK_CAT_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.ADOPTER_ALREADY_TOOK_DOG_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.ADOPTION_SUCCESS_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.ANIMAL_FOUND_BY_NAME_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.CAT_ADOPTERS_FOUND_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.CAT_ADOPTERS_NOT_FOUND_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.CAT_ALREADY_HAS_ADOPTER_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.CAT_NOT_FOUND_BY_ID_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.CAT_NOT_FOUND_BY_NAME_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.DATA_IS_NOT_CORRECT_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.DOG_ADOPTERS_FOUND_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.DOG_ADOPTERS_NOT_FOUND_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.DOG_ALREADY_HAS_ADOPTER_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.DOG_NOT_FOUND_BY_ID_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.DOG_NOT_FOUND_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.USER_FOUND_BY_PHONE_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.USER_NOT_FOUND_BY_ID_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.USER_NOT_FOUND_BY_PHONE_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.WAY_BACK_TEXT;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DogRepository dogRepository ;

    @Mock
    private CatRepository catRepository ;

    @Mock
    private DogReportRepository dogReportRepository;

    @Mock
    private CatReportRepository catReportRepository ;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private VolunteerInfoDogRepository volunteerInfoDogRepository;

    @Mock
    private VolunteerInfoCatRepository volunteerInfoCatRepository;

    @InjectMocks
    private VolunteerService volunteerService;

    private ArgumentCaptor<SendMessage> captor;

    @BeforeEach
    public void setUp() {
        captor = ArgumentCaptor.forClass(SendMessage.class);
    }


    @Test
    @DisplayName("Проверка поиска пользователей по номеру телефона, когда нет совпадений")
    public void testFindUsersByPhoneWhenNoMatchingUsers() {
        Long chatId = 123L;
        String phone = "1234567890";

        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        volunteerService.findUsersByPhone(chatId, phone);

        verifyNoInteractions(validationUtils);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(USER_NOT_FOUND_BY_PHONE_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка поиска пользователей по номеру телефона, когда есть совпадения")
    public void testFindUsersByPhoneWhenMatchingUsersFound() {
        Long chatId = 123L;
        String phone = "1234567890";
        User user = new User();
        user.setPhone("1234567890");

        List<User> matchingUsers = new ArrayList<>();
        matchingUsers.add(user);

        when(userRepository.findAll()).thenReturn(matchingUsers);
        when(validationUtils.phoneNumberFormat(phone)).thenReturn(phone);

        volunteerService.findUsersByPhone(chatId, phone);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(USER_FOUND_BY_PHONE_TEXT + matchingUsers + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка, когда списки усыновителей пусты")
    public void testGetAllAdoptersEmptyLists() {
        Long chatId = 123L;
        when(catRepository.getCatAdopters()).thenReturn(Collections.emptyList());
        when(dogRepository.getDogAdopters()).thenReturn(Collections.emptyList());

        volunteerService.getAllAdopters(chatId);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(ADOPTERS_NOT_FOUND_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка, когда есть и усыновители кошек, и усыновители собак")
    public void testGetAllAdoptersBothListsNotEmpty() {
        Long chatId = 123L;
        List<Long> catAdopters = Arrays.asList(1L, 2L, 3L);
        List<Long> dogAdopters = Arrays.asList(4L, 5L, 6L);
        when(catRepository.getCatAdopters()).thenReturn(catAdopters);
        when(dogRepository.getDogAdopters()).thenReturn(dogAdopters);

        volunteerService.getAllAdopters(chatId);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(
                CAT_ADOPTERS_FOUND_TEXT + catAdopters + "\n" + DOG_ADOPTERS_FOUND_TEXT + dogAdopters + "\n" + WAY_BACK_TEXT,
                sendMessage.getParameters().get("text")
        );
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка, когда есть только усыновители кошек")
    public void testGetAllAdoptersOnlyCatAdopters() {
        Long chatId = 123L;
        List<Long> catAdopters = Arrays.asList(1L, 2L, 3L);
        when(catRepository.getCatAdopters()).thenReturn(catAdopters);
        when(dogRepository.getDogAdopters()).thenReturn(Collections.emptyList());

        volunteerService.getAllAdopters(chatId);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(
                CAT_ADOPTERS_FOUND_TEXT + catAdopters + "\n" + DOG_ADOPTERS_NOT_FOUND_TEXT + WAY_BACK_TEXT,
                sendMessage.getParameters().get("text")
        );
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка, когда есть только усыновители собак")
    public void testGetAllAdoptersOnlyDogAdopters() {
        Long chatId = 123L;
        List<Long> dogAdopters = Arrays.asList(4L, 5L, 6L);
        when(catRepository.getCatAdopters()).thenReturn(Collections.emptyList());
        when(dogRepository.getDogAdopters()).thenReturn(dogAdopters);

        volunteerService.getAllAdopters(chatId);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(
                DOG_ADOPTERS_FOUND_TEXT + dogAdopters + "\n" + CAT_ADOPTERS_NOT_FOUND_TEXT + WAY_BACK_TEXT,
                sendMessage.getParameters().get("text")
        );
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка, когда сообщение пользователя имеет некорректный формат")
    public void testFindAnimalByNameIncorrectFormat() {
        Long chatId = 123L;
        String messageText = "Кошка,Барсик,ДругойПараметр";

        volunteerService.findAnimalByName(chatId, messageText);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка, когда в сообщении указан вид 'Кошка' и животное с заданным именем найдено")
    public void testFindAnimalByNameCatFound() {
        Long chatId = 123L;
        String messageText = "Кошка,Барсик";
        Cat cat = new Cat();
        cat.setNickname("Барсик");
        List<Cat> catList = List.of(cat);

        when(catRepository.findAll()).thenReturn(catList);

        volunteerService.findAnimalByName(chatId, messageText);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(ANIMAL_FOUND_BY_NAME_TEXT + catList + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка, когда в сообщении указан вид 'Кошка', но животное с заданным именем не найдено")
    public void testFindAnimalByNameCatNotFound() {
        Long chatId = 123L;
        String messageText = "Кошка,Барсик";

        when(catRepository.findAll()).thenReturn(Collections.emptyList());

        volunteerService.findAnimalByName(chatId, messageText);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(CAT_NOT_FOUND_BY_NAME_TEXT + "Барсик" + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка, когда в сообщении указан вид 'Собака' и животное с заданным именем найдено")
    public void testFindAnimalByNameDogFound() {
        Long chatId = 123L;
        String messageText = "Собака,Рекс";
        Dog dog = new Dog();
        dog.setNickname("Рекс");
        List<Dog> dogList = List.of(dog);

        when(dogRepository.findAll()).thenReturn(dogList);

        volunteerService.findAnimalByName(chatId, messageText);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(ANIMAL_FOUND_BY_NAME_TEXT + dogList + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка, когда в сообщении указан вид 'Собака', но животное с заданным именем не найдено")
    public void testFindAnimalByNameDogNotFound() {
        Long chatId = 123L;
        String messageText = "Собака,Рекс";

        when(dogRepository.findAll()).thenReturn(Collections.emptyList());

        volunteerService.findAnimalByName(chatId, messageText);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DOG_NOT_FOUND_TEXT + "Рекс" + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка, когда в сообщении указан неизвестный вид животного")
    public void testFindAnimalByNameUnknownAnimal() {
        Long chatId = 123L;
        String messageText = "Попугай,Кеша";

        volunteerService.findAnimalByName(chatId, messageText);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка, когда у кошки уже есть усыновитель")
    public void testCheckOneCatToOneAdopterConditionCatHasAdopter() {
        Long chatId = 123L;
        Cat cat = new Cat();
        User newAdopter = new User();

        cat.setUser(newAdopter);

        boolean result = volunteerService.checkOneCatToOneAdopterCondition(chatId, cat, newAdopter);

        assertFalse(result);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(CAT_ALREADY_HAS_ADOPTER_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка, когда у нового усыновителя уже есть кошка")
    public void testCheckOneCatToOneAdopterConditionAdopterHasCat() {
        Long chatId = 123L;
        Cat cat = new Cat();
        User newAdopter = new User();
        newAdopter.setCat(new Cat());

        boolean result = volunteerService.checkOneCatToOneAdopterCondition(chatId, cat, newAdopter);

        assertFalse(result);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(ADOPTER_ALREADY_TOOK_CAT_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка, когда условия соблюдаются и новый усыновитель может взять кошку")
    public void testCheckOneCatToOneAdopterConditionValidCondition() {
        Long chatId = 123L;
        Cat cat = new Cat();
        User newAdopter = new User();

        boolean result = volunteerService.checkOneCatToOneAdopterCondition(chatId, cat, newAdopter);

        assertTrue(result);

        verifyNoInteractions(telegramBot);
    }

    @Test
    @DisplayName("Проверка, когда у собаки уже есть усыновитель")
    public void testCheckOneDogToOneAdopterConditionDogHasAdopter() {
        Long chatId = 123L;
        Dog dog = new Dog();
        User newAdopter = new User();

        dog.setUser(newAdopter);

        boolean result = volunteerService.checkOneDogToOneAdopterCondition(chatId, dog, newAdopter);

        assertFalse(result);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DOG_ALREADY_HAS_ADOPTER_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка, когда у нового усыновителя уже есть собака")
    public void testCheckOneDogToOneAdopterConditionAdopterHasDog() {
        Long chatId = 123L;
        Dog dog = new Dog();
        User newAdopter = new User();
        newAdopter.setDog(new Dog());

        boolean result = volunteerService.checkOneDogToOneAdopterCondition(chatId, dog, newAdopter);

        assertFalse(result);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(ADOPTER_ALREADY_TOOK_DOG_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка, когда условия соблюдаются и новый усыновитель может взять собаку")
    public void testCheckOneDogToOneAdopterConditionValidCondition() {
        Long chatId = 123L;
        Dog dog = new Dog();
        User newAdopter = new User();

        boolean result = volunteerService.checkOneDogToOneAdopterCondition(chatId, dog, newAdopter);

        assertTrue(result);

        verifyNoInteractions(telegramBot);
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка обновления данных о кошке, когда кошка не найдена по ID")
    public void testUpdateCatDataCatNotFound() {
        Long chatId = 123L;
        Long userId = 456L;
        Long catId = 789L;

        when(catRepository.findById(catId)).thenReturn(Optional.empty());

        volunteerService.updateCatData(chatId, userId, catId);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(CAT_NOT_FOUND_BY_ID_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Проверка обновления данных о кошке, когда пользователь не найден по ID")
    public void testUpdateCatDataUserNotFound() {
        Long chatId = 123L;
        Long userId = 456L;
        Long catId = 789L;
        Cat cat = new Cat();

        when(catRepository.findById(catId)).thenReturn(Optional.of(cat));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        volunteerService.updateCatData(chatId, userId, catId);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(USER_NOT_FOUND_BY_ID_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
        verify(catRepository).findById(catId);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Проверка обновления данных о кошке, когда условия соблюдаются и данные обновлены")
    public void testUpdateCatDataValidCondition() {
        Long chatId = 123L;
        Long userId = 456L;
        Long catId = 789L;
        Cat cat = new Cat();
        User user = new User();

        when(catRepository.findById(catId)).thenReturn(Optional.of(cat));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        volunteerService.updateCatData(chatId, userId, catId);

        verify(catRepository).save(cat);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(ADOPTION_SUCCESS_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка обновления данных о собаке, когда собака не найдена по ID")
    public void testUpdateDogDataDogNotFound() {
        Long chatId = 123L;
        Long userId = 456L;
        Long dogId = 789L;

        when(dogRepository.findById(dogId)).thenReturn(Optional.empty());

        volunteerService.updateDogData(chatId, userId, dogId);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DOG_NOT_FOUND_BY_ID_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Проверка обновления данных о собаке, когда пользователь не найден по ID")
    public void testUpdateDogDataUserNotFound() {
        Long chatId = 123L;
        Long userId = 456L;
        Long dogId = 789L;
        Dog dog = new Dog();

        when(dogRepository.findById(dogId)).thenReturn(Optional.of(dog));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        volunteerService.updateDogData(chatId, userId, dogId);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(USER_NOT_FOUND_BY_ID_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
        verify(dogRepository).findById(dogId);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Проверка обновления данных о собаке, когда условия соблюдаются и данные обновлены")
    public void testUpdateDogDataValidCondition() {
        Long chatId = 123L;
        Long userId = 456L;
        Long dogId = 789L;
        Dog dog = new Dog();
        User user = new User();

        when(dogRepository.findById(dogId)).thenReturn(Optional.of(dog));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        volunteerService.updateDogData(chatId, userId, dogId);

        verify(dogRepository).save(dog);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(ADOPTION_SUCCESS_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка разбора сообщения при неправильном формате данных")
    public void testAllowUserBecomeAdopterIncorrectDataFormat() {
        Long chatId = 123L;
        String messageText = "123, Кошка";

        volunteerService.allowUserBecomeAdopter(chatId, messageText);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка разбора сообщения при некорректных числах")
    public void testAllowUserBecomeAdopterInvalidNumbers() {
        Long chatId = 123L;
        String messageText = "abc, Кошка, xyz";

        volunteerService.allowUserBecomeAdopter(chatId, messageText);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка вызова метода updateCatData при выборе Кошки и корректных данных")
    public void testAllowUserBecomeAdopterChooseCat() {
        Long chatId = 123L;
        String messageText = "456, Кошка, 789";
        Long userId = 456L;
        Long catId = 789L;
        Cat cat = new Cat();
        User user = new User();

        when(catRepository.findById(catId)).thenReturn(Optional.of(cat));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        volunteerService.allowUserBecomeAdopter(chatId, messageText);

        verify(catRepository).save(cat);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(ADOPTION_SUCCESS_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка вызова метода updateDogData при выборе Собаки и корректных данных")
    public void testAllowUserBecomeAdopterChooseDog() {
        Long chatId = 123L;
        String messageText = "456, Собака, 789";
        Long userId = 456L;
        Long dogId = 789L;
        Dog dog = new Dog();
        User user = new User();

        when(dogRepository.findById(dogId)).thenReturn(Optional.of(dog));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        volunteerService.allowUserBecomeAdopter(chatId, messageText);

        verify(dogRepository).save(dog);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(ADOPTION_SUCCESS_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка вызова метода execute при некорректном выборе животного")
    public void testAllowUserBecomeAdopterInvalidPetChoice() {
        Long chatId = 123L;
        String messageText = "123, Попугай, 456";

        volunteerService.allowUserBecomeAdopter(chatId, messageText);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

//##################################################################################################################

    @Test
    @DisplayName("Неудачное продление испытательного срока: неверное количество параметров")
    void testIncreaseProbationPeriodInvalidLength() {
        Long chatId = 123L;
        String text = "456, собака";

        volunteerService.increaseProbationPeriod(chatId, text);

        verifyNoInteractions(userRepository, volunteerInfoDogRepository, volunteerInfoCatRepository);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Введите данные в формате: ID пользователя, кошка(или собака), количество дней 14 или 30",
                sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Неудачное продление испытательного срока: пользователь не найден")
    void testIncreaseProbationPeriodUserNotFound() {
        Long chatId = 123L;
        String text = "456, собака, 14";

        when(userRepository.findById(456L)).thenReturn(Optional.empty());

        volunteerService.increaseProbationPeriod(chatId, text);

        verify(userRepository).findById(456L);
        verifyNoInteractions(volunteerInfoDogRepository, volunteerInfoCatRepository);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Не найден пользователь с данным userId", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Неудачное продление испытательного срока: неправильный тип животного")
    void testIncreaseProbationPeriodInvalidPetType() {
        Long chatId = 123L;
        String text = "456, попугай, 14";

        when(userRepository.findById(456L)).thenReturn(Optional.of(new User()));

        volunteerService.increaseProbationPeriod(chatId, text);

        verifyNoInteractions(volunteerInfoDogRepository, volunteerInfoCatRepository);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Неправильный тип животного. Введите данные в формате: ID пользователя, кошка(или собака), количество дней 14 или 30",
                sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Неудачное продление испытательного срока для собаки: информация о собаке не найдена")
    void testIncreaseProbationPeriodDogInfoNotFound() {
        Long chatId = 123L;
        Long userId = 456L;
        String text = userId + ", собака, 14";

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(volunteerInfoDogRepository.findByUserId(userId)).thenReturn(Optional.empty());

        volunteerService.increaseProbationPeriod(chatId, text);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Не найдена информация по отчёту для собаки, для данного юзера\nВозврат в предыдущее меню (/back)\nВыключить бота (/stop)", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
        verify(userRepository).findById(userId);
        verify(volunteerInfoDogRepository).findByUserId(userId);
        verifyNoInteractions(volunteerInfoCatRepository);
    }

    @Test
    @DisplayName("Успешное продление испытательного срока для собаки")
    void testIncreaseProbationPeriodSuccessfulDog() {
        Long chatId = 123L;
        Long userId = 456L;
        int extraDays = 14;
        String text = userId + ", собака, " + extraDays;
        Chat chat = new Chat();
        User user = new User();
        chat.setChatId(chatId);
        user.setChat(chat);
        VolunteerInfoDog volunteerInfoDog = new VolunteerInfoDog();
        volunteerInfoDog.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(volunteerInfoDogRepository.findByUserId(userId)).thenReturn(Optional.of(volunteerInfoDog));

        volunteerService.increaseProbationPeriod(chatId, text);

        verify(volunteerInfoDogRepository).save(volunteerInfoDog);
        verify(telegramBot, times(2)).execute(captor.capture());
        List<SendMessage> sendMessages = captor.getAllValues();
        assertEquals("Испытательный срок успешно продлён.\n" +
                "Возврат в предыдущее меню (/back)\n" +
                "Выключить бота (/stop)", sendMessages.get(0).getParameters().get("text"));
        assertEquals(chatId, sendMessages.get(0).getParameters().get("chat_id"));
        assertEquals("Испытательный срок успешно продлён на " + extraDays + " дней", sendMessages.get(1).getParameters().get("text"));
        assertEquals(user.getChat().getChatId(), sendMessages.get(1).getParameters().get("chat_id"));
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Неудачное продление испытательного срока для кошки: информация о кошке не найдена")
    void testIncreaseProbationPeriodCatInfoNotFound() {
        Long chatId = 123L;
        Long userId = 456L;
        String text = userId + ", кошка, 14";

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(volunteerInfoCatRepository.findByUserId(userId)).thenReturn(Optional.empty());

        volunteerService.increaseProbationPeriod(chatId, text);

        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Не найдена информация по отчёту для кошки, для данного юзера\n" +
                "Возврат в предыдущее меню (/back)\n" +
                "Выключить бота (/stop)", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
        verify(userRepository).findById(userId);
        verify(volunteerInfoCatRepository).findByUserId(userId);
        verifyNoInteractions(volunteerInfoDogRepository);
    }

    @Test
    @DisplayName("Успешное продление испытательного срока для кошки")
    void testIncreaseProbationPeriodSuccessfulCat() {
        Long chatId = 123L;
        Long userId = 456L;
        int extraDays = 30;
        String text = userId + ", кошка, " + extraDays;
        Chat chat = new Chat();
        User user = new User();
        chat.setChatId(chatId);
        user.setChat(chat);
        VolunteerInfoCat volunteerInfoCat = new VolunteerInfoCat();
        volunteerInfoCat.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(volunteerInfoCatRepository.findByUserId(userId)).thenReturn(Optional.of(volunteerInfoCat));

        volunteerService.increaseProbationPeriod(chatId, text);

        verify(volunteerInfoCatRepository).save(volunteerInfoCat);
        verify(telegramBot, times(2)).execute(captor.capture());
        List<SendMessage> sendMessages = captor.getAllValues();
        assertEquals("Испытательный срок успешно продлён.\n" +
                "Возврат в предыдущее меню (/back)\n" +
                "Выключить бота (/stop)", sendMessages.get(0).getParameters().get("text"));
        assertEquals(chatId, sendMessages.get(0).getParameters().get("chat_id"));
        assertEquals("Испытательный срок успешно продлён на " + extraDays + " дней", sendMessages.get(1).getParameters().get("text"));
        assertEquals(user.getChat().getChatId(), sendMessages.get(1).getParameters().get("chat_id"));
        verify(userRepository).findById(userId);
    }

    //##################################################################################################################

    @Test
    @DisplayName("Неудачное попытка отдать животное: неверное количество параметров")
    void testGiveAnimalAwayInvalidLength() {
        Long chatId = 123L;
        String text = "456, собака, дополнительный параметр";

        volunteerService.giveAnimalAway(chatId, text);

        verifyNoInteractions(userRepository, volunteerInfoDogRepository, volunteerInfoCatRepository, dogReportRepository, catReportRepository, dogRepository, catRepository);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Введите данные в формате: ID пользователя, кошка(или собака)", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Неудачное отдать забрать животное: пользователь не найден")
    void testGiveAnimalAwayUserNotFound() {
        Long chatId = 123L;
        Long userId = 456L;
        String text = userId + ", собака";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        volunteerService.giveAnimalAway(chatId, text);

        verifyNoInteractions(volunteerInfoDogRepository, volunteerInfoCatRepository, dogReportRepository, catReportRepository, dogRepository, catRepository);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Не найден пользователь с данным userId", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Неудачное попытка отдать животное: неправильный тип животного")
    void testGiveAnimalAwayInvalidPetType() {
        Long chatId = 123L;
        Long userId = 456L;
        String text = userId + ", хомяк";

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        volunteerService.giveAnimalAway(chatId, text);

        verifyNoInteractions(volunteerInfoDogRepository, volunteerInfoCatRepository, dogReportRepository, catReportRepository, dogRepository, catRepository);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Неправильный тип животного. Введите данные в формате: ID пользователя, кошка(или собака)", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Успешная попытка отдать собаку")
    void testGiveAnimalAwayDogSuccess() {
        Long chatId = 123L;
        Long userId = 456L;
        String text = userId + ", собака";

        Chat chat = new Chat();
        User user = new User();
        chat.setChatId(chatId);
        user.setChat(chat);
        Dog dog = new Dog();
        dog.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        volunteerService.giveAnimalAway(chatId, text);

        verify(dogRepository).deleteByUserId(userId);
        verify(dogReportRepository).deleteByUserId(userId);
        verify(volunteerInfoDogRepository).deleteByUserId(userId);
        verify(telegramBot, times(2)).execute(captor.capture());
        List<SendMessage> sendMessages = captor.getAllValues();
        assertEquals("Питомец успешно отдан.\n" +
                "Возврат в предыдущее меню (/back)\n" +
                "Выключить бота (/stop)", sendMessages.get(0).getParameters().get("text"));
        assertEquals(chatId, sendMessages.get(0).getParameters().get("chat_id"));
        assertEquals("УРА!!! Испытательный срок успешно пройден", sendMessages.get(1).getParameters().get("text"));
        assertEquals(user.getChat().getChatId(), sendMessages.get(1).getParameters().get("chat_id"));
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Успешная попытка отдать кошку")
    void testGiveAnimalAwayCatSuccess() {
        Long chatId = 123L;
        Long userId = 456L;
        String text = userId + ", кошка";

        Chat chat = new Chat();
        User user = new User();
        chat.setChatId(chatId);
        user.setChat(chat);
        Cat cat = new Cat();
        cat.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        volunteerService.giveAnimalAway(chatId, text);

        verify(catRepository).deleteByUserId(userId);
        verify(catReportRepository).deleteByUserId(userId);
        verify(volunteerInfoCatRepository).deleteByUserId(userId);
        verify(telegramBot, times(2)).execute(captor.capture());
        List<SendMessage> sendMessages = captor.getAllValues();
        assertEquals("Питомец успешно отдан.\n" +
                "Возврат в предыдущее меню (/back)\n" +
                "Выключить бота (/stop)", sendMessages.get(0).getParameters().get("text"));
        assertEquals(chatId, sendMessages.get(0).getParameters().get("chat_id"));
        assertEquals("УРА!!! Испытательный срок успешно пройден", sendMessages.get(1).getParameters().get("text"));
        assertEquals(user.getChat().getChatId(), sendMessages.get(1).getParameters().get("chat_id"));
        verify(userRepository).findById(userId);
    }

    //##################################################################################################################

    @Test
    @DisplayName("Неудачная попытка вернуть питомца: неверное количество параметров")
    void testTakeBackAnimalInvalidLength() {
        Long chatId = 123L;
        String text = "456, собака, дополнительный параметр";

        volunteerService.takeBackAnimal(chatId, text);

        verifyNoInteractions(userRepository, dogRepository, catRepository, volunteerInfoDogRepository, volunteerInfoCatRepository, dogReportRepository, catReportRepository);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Введите данные в формате: ID пользователя, кошка(или собака)", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Неудачная попытка вернуть питомца: пользователь не найден")
    void testTakeBackAnimalUserNotFound() {
        Long chatId = 123L;
        Long userId = 456L;
        String text = userId + ", собака";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        volunteerService.takeBackAnimal(chatId, text);

        verify(userRepository).findById(userId);
        verifyNoInteractions(dogRepository, catRepository, volunteerInfoDogRepository, volunteerInfoCatRepository, dogReportRepository, catReportRepository);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Не найден пользователь с данным userId", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Неудачная попытка вернуть питомца: неправильный тип животного")
    void testTakeBackAnimalInvalidPetType() {
        Long chatId = 123L;
        Long userId = 456L;
        String text = userId + ", попугай";

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        volunteerService.takeBackAnimal(chatId, text);

        verify(userRepository).findById(userId);
        verifyNoInteractions(dogRepository, catRepository, volunteerInfoDogRepository, volunteerInfoCatRepository, dogReportRepository, catReportRepository);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Неправильный тип животного. Введите данные в формате: ID пользователя, кошка(или собака)", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Неудачная попытка вернуть собаку: не найдена собака, у которой есть усыновитель")
    void testTakeBackAnimalDogNotFound() {
        Long chatId = 123L;
        Long userId = 456L;
        String text = userId + ", собака";

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(dogRepository.findByUserId(userId)).thenReturn(Optional.empty());

        volunteerService.takeBackAnimal(chatId, text);

        verify(userRepository).findById(userId);
        verify(dogRepository).findByUserId(userId);
        verifyNoInteractions(catRepository, volunteerInfoDogRepository, volunteerInfoCatRepository, dogReportRepository, catReportRepository);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Не найдена собака, у которой есть усыновитель", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Успешное возвращение собаки в приют")
    void testTakeBackAnimalDogSuccess() {
        Long chatId = 123L;
        Long userId = 456L;
        String text = userId + ", собака";

        Chat chat = new Chat();
        User user = new User();
        chat.setChatId(chatId);
        user.setChat(chat);
        Dog dog = new Dog();
        dog.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(dogRepository.findByUserId(userId)).thenReturn(Optional.of(dog));

        volunteerService.takeBackAnimal(chatId, text);

        verify(dogRepository).save(dog);
        verify(dogReportRepository).deleteByUserId(userId);
        verify(volunteerInfoDogRepository).deleteByUserId(userId);
        verify(telegramBot, times(2)).execute(captor.capture());
        List<SendMessage> sendMessages = captor.getAllValues();
        assertEquals("Питомец вернулся обратно в приют.\n" +
                "Возврат в предыдущее меню (/back)\n" +
                "Выключить бота (/stop)", sendMessages.get(0).getParameters().get("text"));
        assertEquals(chatId, sendMessages.get(0).getParameters().get("chat_id"));
        assertEquals("Приют забрал у вас собаку обратно", sendMessages.get(1).getParameters().get("text"));
        assertEquals(user.getChat().getChatId(), sendMessages.get(1).getParameters().get("chat_id"));
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Неудачная попытка вернуть кошку: не найдена кошка, у которой есть усыновитель")
    void testTakeBackAnimalCatNotFound() {
        Long chatId = 123L;
        Long userId = 456L;
        String text = userId + ", кошка";

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(catRepository.findByUserId(userId)).thenReturn(Optional.empty());

        volunteerService.takeBackAnimal(chatId, text);

        verify(userRepository).findById(userId);
        verify(catRepository).findByUserId(userId);
        verifyNoInteractions(dogRepository, volunteerInfoDogRepository, volunteerInfoCatRepository, dogReportRepository, catReportRepository);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Не найдена кошка, у которой есть усыновитель", sendMessage.getParameters().get("text"));
        assertEquals(chatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Успешное возвращение кошки в приют")
    void testTakeBackAnimalCatSuccess() {
        Long chatId = 123L;
        Long userId = 456L;
        String text = userId + ", кошка";

        Chat chat = new Chat();
        User user = new User();
        chat.setChatId(chatId);
        user.setChat(chat);
        Cat cat = new Cat();
        cat.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(catRepository.findByUserId(userId)).thenReturn(Optional.of(cat));

        volunteerService.takeBackAnimal(chatId, text);

        verify(catRepository).save(cat);
        verify(catReportRepository).deleteByUserId(userId);
        verify(volunteerInfoCatRepository).deleteByUserId(userId);
        verify(telegramBot, times(2)).execute(captor.capture());
        List<SendMessage> sendMessages = captor.getAllValues();
        assertEquals("Питомец вернулся обратно в приют.\n" +
                "Возврат в предыдущее меню (/back)\n" +
                "Выключить бота (/stop)", sendMessages.get(0).getParameters().get("text"));
        assertEquals(chatId, sendMessages.get(0).getParameters().get("chat_id"));
        assertEquals("Приют забрал у вас кошку обратно", sendMessages.get(1).getParameters().get("text"));
        assertEquals(user.getChat().getChatId(), sendMessages.get(1).getParameters().get("chat_id"));
        verify(userRepository).findById(userId);
    }
}