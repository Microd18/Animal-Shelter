package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.entity.*;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.enums.UserReportStates;
import pro.sky.AnimalShelter.repository.*;
import pro.sky.AnimalShelter.utils.ConsoleOutputCapture;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.CAT;
import static pro.sky.AnimalShelter.enums.BotCommand.DOG;
import static pro.sky.AnimalShelter.enums.UserReportStates.*;

@ExtendWith(MockitoExtension.class)
class UserReportServiceTest {


    @Mock
    private CatReportRepository catReportRepository;

    @Mock
    private DogReportRepository dogReportRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatStateService chatStateService;

    @Mock
    private UserReportStateService userReportStateService;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private CatPhotoRepository catPhotoRepository;

    @Mock
    private DogPhotoRepository dogPhotoRepository;

    @Mock
    private CatRepository catRepository;

    @Mock
    private DogRepository dogRepository;

    @InjectMocks
    private UserReportService userReportService;

    private ConsoleOutputCapture outputCapture;

    @BeforeEach
    public void setUp() {
        outputCapture = new ConsoleOutputCapture();
        Chat chat = new Chat();
        chat.setId(123L);
        User user = new User();
        user.setId(456L);
        lenient().when(chatRepository.findByChatId(123L)).thenReturn(Optional.of(chat));
        lenient().when(userRepository.findByChatId(123L)).thenReturn(Optional.of(user));
    }

    @AfterEach
    public void cleanup() {
        outputCapture.stopCapture();
    }


    @ParameterizedTest
    @EnumSource(UserReportStates.class)
    @DisplayName("Проверка на сохранение отчета для собаки, позитивные тесты + негативный с неправильным состоянием отчёта")
    public void testSaveReportForDog(UserReportStates state) {
        lenient().when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(DOG);
        lenient().when(dogRepository.findByUserId(456L)).thenReturn(Optional.of(new Dog()));
        lenient().when(dogReportRepository.findByUserId(456L)).thenReturn(Optional.of(new DogReport()));

        userReportService.saveReportData(123L, "Текст отчета", state);

        if (state == BEHAVIOR) {
            verify(dogReportRepository, times(1)).save(any());
            verify(dogReportRepository, times(1)).findByUserId(456L);
            verify(telegramBot, times(1)).execute(any(SendMessage.class));
            verify(userReportStateService, times(1)).clearUserReportStates(123L);
            assertEquals(outputCapture.getCapturedOutput(), "saveReportData method was invoked");
        } else if (state == RATION) {
            verify(dogReportRepository, times(1)).save(any());
            verify(dogReportRepository, times(1)).findByUserId(456L);
            verify(userReportStateService, times(1)).updateUserReportState(123L, WELL_BEING);
            verify(telegramBot, times(1)).execute(any(SendMessage.class));
            assertEquals(outputCapture.getCapturedOutput(), "saveReportData method was invoked");
        } else if (state == WELL_BEING) {
            verify(dogReportRepository, times(1)).save(any());
            verify(dogReportRepository, times(1)).findByUserId(456L);
            verify(userReportStateService, times(1)).updateUserReportState(123L, BEHAVIOR);
            verify(telegramBot, times(1)).execute(any(SendMessage.class));
            assertEquals(outputCapture.getCapturedOutput(), "saveReportData method was invoked");
        } else if (state == PHOTO) {
            verifyNoInteractions(chatStateService, userRepository, catRepository, dogRepository,
                    catReportRepository, dogReportRepository, userReportStateService, telegramBot);
        }
    }

    @ParameterizedTest
    @EnumSource(UserReportStates.class)
    @DisplayName("Проверка на сохранение отчета для кошки, позитивные тесты + негативный с неправильным состоянием отчёта")
    public void testSaveReportForCat(UserReportStates state) {
        lenient().when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(CAT);
        lenient().when(catRepository.findByUserId(456L)).thenReturn(Optional.of(new Cat()));
        lenient().when(catReportRepository.findByUserId(456L)).thenReturn(Optional.of(new CatReport()));

        userReportService.saveReportData(123L, "Текст отчета", state);

        if (state == BEHAVIOR) {
            verify(catReportRepository, times(1)).save(any());
            verify(catReportRepository, times(1)).findByUserId(456L);
            verify(telegramBot, times(1)).execute(any(SendMessage.class));
            verify(userReportStateService, times(1)).clearUserReportStates(123L);
            assertEquals(outputCapture.getCapturedOutput(), "saveReportData method was invoked");
        } else if (state == RATION) {
            verify(catReportRepository, times(1)).save(any());
            verify(catReportRepository, times(1)).findByUserId(456L);
            verify(userReportStateService, times(1)).updateUserReportState(123L, WELL_BEING);
            verify(telegramBot, times(1)).execute(any(SendMessage.class));
            assertEquals(outputCapture.getCapturedOutput(), "saveReportData method was invoked");
        } else if (state == WELL_BEING) {
            verify(catReportRepository, times(1)).save(any());
            verify(catReportRepository, times(1)).findByUserId(456L);
            verify(userReportStateService, times(1)).updateUserReportState(123L, BEHAVIOR);
            verify(telegramBot, times(1)).execute(any(SendMessage.class));
            assertEquals(outputCapture.getCapturedOutput(), "saveReportData method was invoked");
        } else if (state == PHOTO) {
            verifyNoInteractions(chatStateService, userRepository, catRepository, dogRepository,
                    catReportRepository, dogReportRepository, userReportStateService, telegramBot);
        }
    }

    @Test
    @DisplayName("Проверка на сохранение отчета когда чат не найден")
    public void testSaveReportNoChat() {
        when(chatRepository.findByChatId(123L)).thenReturn(Optional.empty());

        userReportService.saveReportData(123L, "Текст отчета", BEHAVIOR);

        assertEquals(outputCapture.getCapturedOutput(), "saveReportData method was invoked");
        verifyNoInteractions(chatStateService, userRepository, catRepository, dogRepository,
                catReportRepository, dogReportRepository, userReportStateService, telegramBot);
    }

    @Test
    @DisplayName("Проверка на сохранение отчета когда нет данных о пользователе")
    public void testSaveReportNoUser() {
        when(userRepository.findByChatId(123L)).thenReturn(Optional.empty());

        userReportService.saveReportData(123L, "Текст отчета", BEHAVIOR);

        assertEquals(outputCapture.getCapturedOutput(), "saveReportData method was invoked");
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verifyNoInteractions(catRepository, dogRepository, catReportRepository,
                dogReportRepository, userReportStateService);
    }

    @Test
    @DisplayName("Проверка на сохранение отчета когда нет последнего состояния кошки или собаки")
    public void testSaveReportNoLastStateCatOrDogByChatId() {
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(null);

        userReportService.saveReportData(123L, "Текст отчета", BEHAVIOR);

        assertEquals(outputCapture.getCapturedOutput(), "saveReportData method was invoked");
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(catRepository, times(1)).findByUserId(456L);
        verify(dogRepository, times(1)).findByUserId(456L);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verifyNoInteractions(catReportRepository, dogReportRepository, userReportStateService);
    }

    @Test
    @DisplayName("Проверка на сохранение отчёта когда у пользователя есть кошка и собака, но нет последнего состояния кошки или собаки")
    public void testSaveReportUserHasCatAndHasDogNoLastStateCatOrDogByChatId() {
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(null);
        when(catRepository.findByUserId(456L)).thenReturn(Optional.of(new Cat()));
        when(dogRepository.findByUserId(456L)).thenReturn(Optional.of(new Dog()));

        userReportService.saveReportData(123L, "Текст отчета", BEHAVIOR);

        assertEquals(outputCapture.getCapturedOutput(), "saveReportData method was invoked");
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(catRepository, times(1)).findByUserId(456L);
        verify(dogRepository, times(1)).findByUserId(456L);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verifyNoInteractions(catReportRepository, dogReportRepository, userReportStateService);
    }

    @Test
    @DisplayName("Проверка на сохранение отчета по кошке когда у пользователя нет кошки")
    public void testSaveCatReportUserHasNoCat() {
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(CAT);
        when(catRepository.findByUserId(456L)).thenReturn(Optional.empty());

        userReportService.saveReportData(123L, "Текст отчета", BEHAVIOR);

        assertEquals(outputCapture.getCapturedOutput(), "saveReportData method was invoked");
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(catRepository, times(1)).findByUserId(456L);
        verify(dogRepository, times(1)).findByUserId(456L);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verifyNoInteractions(dogReportRepository, catReportRepository, userReportStateService);
    }

    @Test
    @DisplayName("Проверка на сохранение отчета по собаке когда у пользователя нет собаки")
    public void testSaveDogReportUserHasNoDog() {
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(DOG);
        when(dogRepository.findByUserId(456L)).thenReturn(Optional.empty());

        userReportService.saveReportData(123L, "Текст отчета", BEHAVIOR);

        assertEquals(outputCapture.getCapturedOutput(), "saveReportData method was invoked");
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(catRepository, times(1)).findByUserId(456L);
        verify(dogRepository, times(1)).findByUserId(456L);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verifyNoInteractions(dogReportRepository, catReportRepository, userReportStateService);
    }

    //###############################################################################################################

    @Test
    @DisplayName("Проверка на сохранение фото кошки в отчёт по кошке")
    public void testSavePhotoForCatReport() throws Exception {
        PhotoSize fakePhoto = new PhotoSize();
        Field fileIdField = PhotoSize.class.getDeclaredField("file_id");
        fileIdField.setAccessible(true);
        fileIdField.set(fakePhoto, "fake_file_id");
        Field fileSizeField = PhotoSize.class.getDeclaredField("file_size");
        fileSizeField.setAccessible(true);
        fileSizeField.set(fakePhoto, 1000L);
        GetFileResponse fakeGetFileResponse = mock(GetFileResponse.class);
        PhotoSize[] photoSizes = {fakePhoto};
        fileSizeField.set(fakePhoto, (long) (UserReportService.MAX_SIZE_IN_BYTES - 100));

        when(fakeGetFileResponse.isOk()).thenReturn(true);
        when(telegramBot.execute(any(GetFile.class))).thenReturn(fakeGetFileResponse);
        when(telegramBot.getFileContent(fakeGetFileResponse.file())).thenReturn(new byte[]{76, 69, 0, 0, 0, 0, 0, 0, 0, 97, 99, 115, 112, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(BotCommand.CAT);
        when(catRepository.findByUserId(456L)).thenReturn(Optional.of(new Cat()));
        when(dogRepository.findByUserId(456L)).thenReturn(Optional.empty());
        when(catReportRepository.findByUserId(456L)).thenReturn(Optional.of(new CatReport()));

        userReportService.savePhotoForReport(123L, photoSizes);

        verify(catPhotoRepository, times(1)).save(any());
        verify(telegramBot, times(2)).execute(any());
        verify(telegramBot, times(1)).getFileContent(any());
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(catRepository, times(1)).findByUserId(456L);
        verify(dogRepository, times(1)).findByUserId(456L);
        verify(dogReportRepository, times(0)).findByUserId(456L);
        verify(catReportRepository, times(1)).findByUserId(456L);
        verify(userReportStateService, times(1)).updateUserReportState(123L, RATION);
    }

    @Test
    @DisplayName("Проверка на сохранение фото собаки в отчёт по собаке")
    public void testSavePhotoForDogReport() throws Exception {
        PhotoSize fakePhoto = new PhotoSize();
        Field fileIdField = PhotoSize.class.getDeclaredField("file_id");
        fileIdField.setAccessible(true);
        fileIdField.set(fakePhoto, "fake_file_id");
        Field fileSizeField = PhotoSize.class.getDeclaredField("file_size");
        fileSizeField.setAccessible(true);
        fileSizeField.set(fakePhoto, 1000L);
        GetFileResponse fakeGetFileResponse = mock(GetFileResponse.class);
        PhotoSize[] photoSizes = {fakePhoto};
        fileSizeField.set(fakePhoto, (long) (UserReportService.MAX_SIZE_IN_BYTES - 100));

        when(fakeGetFileResponse.isOk()).thenReturn(true);
        when(telegramBot.execute(any(GetFile.class))).thenReturn(fakeGetFileResponse);
        when(telegramBot.getFileContent(fakeGetFileResponse.file())).thenReturn(new byte[]{76, 69, 0, 0, 0, 0, 0, 0, 0, 97, 99, 115, 112, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(DOG);
        when(catRepository.findByUserId(456L)).thenReturn(Optional.empty());
        when(dogRepository.findByUserId(456L)).thenReturn(Optional.of(new Dog()));
        when(dogReportRepository.findByUserId(456L)).thenReturn(Optional.of(new DogReport()));

        userReportService.savePhotoForReport(123L, photoSizes);

        verify(dogPhotoRepository, times(1)).save(any());
        verify(telegramBot, times(2)).execute(any());
        verify(telegramBot, times(1)).getFileContent(any());
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(catRepository, times(1)).findByUserId(456L);
        verify(dogRepository, times(1)).findByUserId(456L);
        verify(dogReportRepository, times(1)).findByUserId(456L);
        verify(catReportRepository, times(0)).findByUserId(456L);
        verify(userReportStateService, times(1)).updateUserReportState(123L, RATION);
    }

    @Test
    @DisplayName("Проверка на сохранение фотографии когда чат не найден")
    public void testSavePhotoForReportWhenChatNotFound() {
        when(chatRepository.findByChatId(123L)).thenReturn(Optional.empty());

        userReportService.savePhotoForReport(123L, new PhotoSize[0]);

        assertEquals(outputCapture.getCapturedOutput(), "savePhotoForReport method was invoked");
        verifyNoInteractions(chatStateService, userRepository, catRepository, dogRepository,
                catReportRepository, dogReportRepository, userReportStateService, telegramBot);
    }


    @Test
    @DisplayName("Проверка на сохранение фотографии когда нет данных о пользователе")
    public void testSavePhotoForReportWhenUserAndChatNotFound() {
        when(userRepository.findByChatId(123L)).thenReturn(Optional.empty());

        userReportService.savePhotoForReport(123L, new PhotoSize[1]);

        assertEquals(outputCapture.getCapturedOutput(), "savePhotoForReport method was invoked");
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verifyNoInteractions(catRepository, dogRepository, catReportRepository, dogReportRepository,
                userReportStateService);
    }

    @Test
    @DisplayName("Проверка на сохранение фотографии когда нет последнего состояния кошки или собаки")
    public void testSavePhotoNoLastStateCatOrDogByChatId() {
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(null);

        userReportService.savePhotoForReport(123L, new PhotoSize[1]);

        assertEquals(outputCapture.getCapturedOutput(), "savePhotoForReport method was invoked");
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(catRepository, times(1)).findByUserId(456L);
        verify(dogRepository, times(1)).findByUserId(456L);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verifyNoInteractions(catReportRepository, dogReportRepository, userReportStateService);
    }

    @Test
    @DisplayName("Проверка на сохранение фотографии когда у пользователя есть кошка и собака, но нет последнего состояния кошки или собаки")
    public void testSavePhotoUserHasCatAndHasDogNoLastStateCatOrDogByChatId() {
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(null);
        when(catRepository.findByUserId(456L)).thenReturn(Optional.of(new Cat()));
        when(dogRepository.findByUserId(456L)).thenReturn(Optional.of(new Dog()));

        userReportService.savePhotoForReport(123L, new PhotoSize[1]);

        assertEquals(outputCapture.getCapturedOutput(), "savePhotoForReport method was invoked");
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(catRepository, times(1)).findByUserId(456L);
        verify(dogRepository, times(1)).findByUserId(456L);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verifyNoInteractions(catReportRepository, dogReportRepository, userReportStateService);
    }

    @Test
    @DisplayName("Проверка на сохранение фото кошки когда у пользователя нет кошки")
    public void testSavePhotoUserHasNoCat() {
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(CAT);
        when(catRepository.findByUserId(456L)).thenReturn(Optional.empty());

        userReportService.savePhotoForReport(123L, new PhotoSize[1]);

        assertEquals(outputCapture.getCapturedOutput(), "savePhotoForReport method was invoked");
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(catRepository, times(1)).findByUserId(456L);
        verify(dogRepository, times(1)).findByUserId(456L);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verifyNoInteractions(dogReportRepository, catReportRepository, userReportStateService);
    }

    @Test
    @DisplayName("Проверка на сохранение фото собаки когда у пользователя нет собаки")
    public void testSavePhotoUserHasNoDog() {
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(DOG);
        when(dogRepository.findByUserId(456L)).thenReturn(Optional.empty());

        userReportService.savePhotoForReport(123L, new PhotoSize[1]);

        assertEquals(outputCapture.getCapturedOutput(), "savePhotoForReport method was invoked");
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(catRepository, times(1)).findByUserId(456L);
        verify(dogRepository, times(1)).findByUserId(456L);
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verifyNoInteractions(dogReportRepository, catReportRepository, userReportStateService);
    }

    @Test
    @DisplayName("Проверка на сохранение фотографии когда массив с фото пустой")
    public void testSavePhotoForReportWhenPhotoSizeIsEmpty() {
        userReportService.savePhotoForReport(123L, new PhotoSize[0]);

        assertEquals(outputCapture.getCapturedOutput(), "savePhotoForReport method was invoked");
        verifyNoInteractions(chatStateService, userRepository, catRepository, dogRepository,
                catReportRepository, dogReportRepository, userReportStateService, telegramBot);
    }

    @Test
    @DisplayName("Проверка на сохранение фотографии когда произошла ошибка преобразования файла в массив байтов")
    public void testSavePhotoForReportWhenWasErrorConvertingFileToByteArray() throws Exception {
        PhotoSize fakePhoto = new PhotoSize();
        Field fileIdField = PhotoSize.class.getDeclaredField("file_id");
        fileIdField.setAccessible(true);
        fileIdField.set(fakePhoto, "fake_file_id");
        Field fileSizeField = PhotoSize.class.getDeclaredField("file_size");
        fileSizeField.setAccessible(true);
        fileSizeField.set(fakePhoto, 1000L);
        GetFileResponse fakeGetFileResponse = mock(GetFileResponse.class);
        PhotoSize[] photoSizes = {fakePhoto};
        fileSizeField.set(fakePhoto, (long) (UserReportService.MAX_SIZE_IN_BYTES - 100));

        when(fakeGetFileResponse.isOk()).thenReturn(true);
        when(chatStateService.getLastStateCatOrDogByChatId(123L)).thenReturn(DOG);
        when(catRepository.findByUserId(456L)).thenReturn(Optional.empty());
        when(dogRepository.findByUserId(456L)).thenReturn(Optional.of(new Dog()));
        when(dogReportRepository.findByUserId(456L)).thenReturn(Optional.of(new DogReport()));
        when(telegramBot.execute(any(GetFile.class))).thenReturn(fakeGetFileResponse);
        when(telegramBot.getFileContent(fakeGetFileResponse.file())).thenReturn(null);

        userReportService.savePhotoForReport(123L, photoSizes);

        verify(telegramBot, times(2)).execute(any());
        verify(telegramBot, times(1)).getFileContent(any());
        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateService, times(1)).getLastStateCatOrDogByChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(catRepository, times(1)).findByUserId(456L);
        verify(dogRepository, times(1)).findByUserId(456L);
        verify(dogReportRepository, times(1)).findByUserId(456L);
        verify(catReportRepository, times(0)).findByUserId(456L);
        verifyNoInteractions(userReportStateService);
    }
}