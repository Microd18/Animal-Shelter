package pro.sky.AnimalShelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.entity.*;
import pro.sky.AnimalShelter.enums.CheckUserReportStates;
import pro.sky.AnimalShelter.repository.CatReportRepository;
import pro.sky.AnimalShelter.repository.DogReportRepository;
import pro.sky.AnimalShelter.repository.VolunteerInfoCatRepository;
import pro.sky.AnimalShelter.repository.VolunteerInfoDogRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.CheckUserReportStates.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.*;

@ExtendWith(MockitoExtension.class)
class CheckUserReportServiceTest {

    @Mock
    private CheckUserReportStateService checkUserReportStateService;

    @Mock
    private VolunteerInfoCatRepository volunteerInfoCatRepository;

    @Mock
    private VolunteerInfoDogRepository volunteerInfoDogRepository;

    @Mock
    private CatReportRepository catReportRepository;

    @Mock
    private DogReportRepository dogReportRepository;

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private CheckUserReportService checkUserReportService;

    @BeforeEach
    public void setUp() {

    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of(123L, "Some message", null),
                Arguments.of(456L, "Another message", ALL_REPORTS),
                Arguments.of(789L, "Yet another message", VIEW_REPORT)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void testDeterminateAndSetCheckReportState(
            Long chatId, String messageText, CheckUserReportStates checkUserReportCurrentState) {
        checkUserReportService.determinateAndSetCheckReportState(chatId, messageText, checkUserReportCurrentState);

        if (checkUserReportCurrentState == null) {
            verify(checkUserReportStateService).updateCheckUserReportState(chatId, ALL_REPORTS);
        } else if (checkUserReportCurrentState == ALL_REPORTS) {
            verify(checkUserReportStateService).updateCheckUserReportState(chatId, VIEW_REPORT);
        } else if (checkUserReportCurrentState == VIEW_REPORT) {
            verify(checkUserReportStateService).updateCheckUserReportState(chatId, EVALUATE_REPORT);
        }
    }

    @Test
    @DisplayName("Проверка вывода сообщения при наличии неоцененных отчетов о собаках")
    public void testGetAllDogReportsWithUnverifiedReports() {
        Long volunteerChatId = 123L;
        List<DogReport> dogReports = new ArrayList<>();
        DogReport unverifiedReport1 = new DogReport();
        DogReport unverifiedReport2 = new DogReport();
        unverifiedReport1.setReportVerified(false);
        unverifiedReport1.setId(1L);
        unverifiedReport2.setReportVerified(false);
        unverifiedReport2.setId(2L);
        dogReports.add(unverifiedReport1);
        dogReports.add(unverifiedReport2);

        when(dogReportRepository.findAll()).thenReturn(dogReports);

        checkUserReportService.getAllDogReports(volunteerChatId);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DOG_REPORT_LIST_TEXT + "[/dog_1, /dog_2]" + "\n", sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(dogReportRepository, times(2)).findAll();
    }


    @Test
    @DisplayName("Проверка вывода сообщения при отсутствии неоцененных отчетов о собаках")
    public void testGetAllDogReportsWithNoUnverifiedReports() {
        Long volunteerChatId = 123L;
        List<DogReport> dogReports = new ArrayList<>();

        when(dogReportRepository.findAll()).thenReturn(dogReports);

        checkUserReportService.getAllDogReports(volunteerChatId);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DOG_REPORT_EMPTY_LIST_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(dogReportRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Проверка вывода сообщения при отсутствии неоцененных отчетов о собаках")
    public void testGetAllDogReportsWithAllReportsVerified() {
        Long volunteerChatId = 123L;
        List<DogReport> dogReports = new ArrayList<>();
        DogReport verifiedReport1 = new DogReport();
        DogReport verifiedReport2 = new DogReport();
        verifiedReport1.setReportVerified(true);
        verifiedReport2.setReportVerified(true);
        dogReports.add(verifiedReport1);
        dogReports.add(verifiedReport2);

        when(dogReportRepository.findAll()).thenReturn(dogReports);

        checkUserReportService.getAllDogReports(volunteerChatId);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DOG_REPORT_EMPTY_LIST_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(dogReportRepository, times(2)).findAll();
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка вывода сообщения при наличии неоцененных отчетов о кошках")
    public void testGetAllCatReportsWithUnverifiedReports() {
        Long volunteerChatId = 123L;
        List<CatReport> catReports = new ArrayList<>();
        CatReport unverifiedReport1 = new CatReport();
        CatReport unverifiedReport2 = new CatReport();
        unverifiedReport1.setReportVerified(false);
        unverifiedReport1.setId(1L);
        unverifiedReport2.setReportVerified(false);
        unverifiedReport2.setId(2L);
        catReports.add(unverifiedReport1);
        catReports.add(unverifiedReport2);

        when(catReportRepository.findAll()).thenReturn(catReports);

        checkUserReportService.getAllCatReports(volunteerChatId);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(CAT_REPORT_LIST_TEXT + "[/cat_1, /cat_2]" + "\n", sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(catReportRepository, times(2)).findAll();
    }


    @Test
    @DisplayName("Проверка вывода сообщения при отсутствии неоцененных отчетов о кошках")
    public void testGetAllCatReportsWithNoUnverifiedReports() {
        Long volunteerChatId = 123L;
        List<CatReport> catReports = new ArrayList<>();

        when(catReportRepository.findAll()).thenReturn(catReports);

        checkUserReportService.getAllCatReports(volunteerChatId);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(CAT_REPORT_EMPTY_LIST_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(catReportRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Проверка вывода сообщения при отсутствии неоцененных отчетов о кошках")
    public void testGetAllCatReportsWithAllReportsVerified() {
        Long volunteerChatId = 123L;
        List<CatReport> catReports = new ArrayList<>();
        CatReport verifiedReport1 = new CatReport();
        CatReport verifiedReport2 = new CatReport();
        verifiedReport1.setReportVerified(true);
        verifiedReport2.setReportVerified(true);
        catReports.add(verifiedReport1);
        catReports.add(verifiedReport2);

        when(catReportRepository.findAll()).thenReturn(catReports);

        checkUserReportService.getAllCatReports(volunteerChatId);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(CAT_REPORT_EMPTY_LIST_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));

        verify(telegramBot, times(1)).execute(any(SendMessage.class));
        verify(catReportRepository, times(2)).findAll();
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка успешной оценки отчёта по собаке")
    public void testEvaluateDogReportSuccess() {
        Long volunteerChatId = 123L;
        Long dogReportId = 1L;
        Integer dogReportRating = 5;

        User user = new User();
        user.setId(100L);

        DogReport dogReport = new DogReport();
        dogReport.setId(dogReportId);
        dogReport.setReportVerified(false);
        dogReport.setUser(user);


        VolunteerInfoDog volunteerInfoDog = new VolunteerInfoDog();
        volunteerInfoDog.setRating(4.0);
        volunteerInfoDog.setAmountOfDays(5);
        volunteerInfoDog.setUser(user);

        when(dogReportRepository.findById(dogReportId)).thenReturn(Optional.of(dogReport));
        when(volunteerInfoDogRepository.findByUserId(user.getId())).thenReturn(Optional.of(volunteerInfoDog));

        checkUserReportService.evaluateDogReport(volunteerChatId, dogReportId, dogReportRating);

        verify(telegramBot).execute(any(SendMessage.class));

        assertTrue(dogReport.getReportVerified());

        assertEquals(4.833333333333333, volunteerInfoDog.getRating(), 0.001);
        assertEquals(5, volunteerInfoDog.getAmountOfDays());
    }

    @Test
    @DisplayName("Проверка отчёта по собаке с отсутствующим отчетом")
    void testEvaluateDogReportDogReportNotFound() {
        Long volunteerChatId = 123L;
        Long dogReportId = 1L;
        Integer dogReportRating = 4;

        when(dogReportRepository.findById(dogReportId)).thenReturn(Optional.empty());

        checkUserReportService.evaluateDogReport(volunteerChatId, dogReportId, dogReportRating);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Собачий отчет с идентификатором "
                + dogReportId + " отсутствует." + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка отчёта по собаке с уже проверенным отчетом")
    void testEvaluateDogReportAlreadyVerified() {
        Long volunteerChatId = 123L;
        Long dogReportId = 1L;
        Integer dogReportRating = 4;
        DogReport dogReport = new DogReport();
        dogReport.setReportVerified(true);

        when(dogReportRepository.findById(dogReportId)).thenReturn(Optional.of(dogReport));

        checkUserReportService.evaluateDogReport(volunteerChatId, dogReportId, dogReportRating);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Этот отчет уже был оценен." + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }


    @Test
    @DisplayName("Проверка отчёта по собаке с отсутствующей информацией о пользователе")
    void testEvaluateDogReportUserInfoNotFound() {
        Long volunteerChatId = 123L;
        Long dogReportId = 1L;
        Integer dogReportRating = 4;
        DogReport dogReport = new DogReport();
        User user = new User();
        user.setId(1L);
        dogReport.setUser(user);
        dogReport.setReportVerified(false);

        when(dogReportRepository.findById(dogReportId)).thenReturn(Optional.of(dogReport));
        when(volunteerInfoDogRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        checkUserReportService.evaluateDogReport(volunteerChatId, dogReportId, dogReportRating);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Не найдена информация в volunteerInfoDog. " +
                "Невозможно проставить оценку."
                + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка успешной оценки отчёта по кошке")
    public void testEvaluateCatReportSuccess() {
        Long volunteerChatId = 123L;
        Long catReportId = 1L;
        Integer dogReportRating = 5;

        User user = new User();
        user.setId(100L);

        CatReport catReport = new CatReport();
        catReport.setId(catReportId);
        catReport.setReportVerified(false);
        catReport.setUser(user);


        VolunteerInfoCat volunteerInfoCat = new VolunteerInfoCat();
        volunteerInfoCat.setRating(4.0);
        volunteerInfoCat.setAmountOfDays(5);
        volunteerInfoCat.setUser(user);

        when(catReportRepository.findById(catReportId)).thenReturn(Optional.of(catReport));
        when(volunteerInfoCatRepository.findByUserId(user.getId())).thenReturn(Optional.of(volunteerInfoCat));

        checkUserReportService.evaluateCatReport(volunteerChatId, catReportId, dogReportRating);

        verify(telegramBot).execute(any(SendMessage.class));

        assertTrue(catReport.getReportVerified());

        assertEquals(4.833333333333333, volunteerInfoCat.getRating(), 0.001);
        assertEquals(5, volunteerInfoCat.getAmountOfDays());
    }

    @Test
    @DisplayName("Проверка отчёта по собаке с отсутствующим отчетом")
    void testEvaluateCatReportDogReportNotFound() {
        Long volunteerChatId = 123L;
        Long catReportId = 1L;
        Integer catReportRating = 4;

        when(catReportRepository.findById(catReportId)).thenReturn(Optional.empty());

        checkUserReportService.evaluateCatReport(volunteerChatId, catReportId, catReportRating);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(CAT_REPORT_NOT_FOUND_BY_ID_TEXT
                + catReportId + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка отчёта по кошке с уже проверенным отчетом")
    void testEvaluateCatReportAlreadyVerified() {
        Long volunteerChatId = 123L;
        Long catReportId = 1L;
        Integer catReportRating = 4;
        CatReport catReport = new CatReport();
        catReport.setReportVerified(true);

        when(catReportRepository.findById(catReportId)).thenReturn(Optional.of(catReport));

        checkUserReportService.evaluateCatReport(volunteerChatId, catReportId, catReportRating);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Этот отчет уже был оценен." + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }


    @Test
    @DisplayName("Проверка отчёта по кошке с отсутствующей информацией о пользователе")
    void testEvaluateCatReportUserInfoNotFound() {
        Long volunteerChatId = 123L;
        Long catReportId = 1L;
        Integer catReportRating = 4;
        CatReport catReport = new CatReport();
        User user = new User();
        user.setId(1L);
        catReport.setUser(user);
        catReport.setReportVerified(false);

        when(catReportRepository.findById(catReportId)).thenReturn(Optional.of(catReport));
        when(volunteerInfoCatRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        checkUserReportService.evaluateCatReport(volunteerChatId, catReportId, catReportRating);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Не найдена информация в volunteerInfoCat. " +
                "Невозможно проставить оценку."
                + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка evaluateReport с некорректными данными")
    void testEvaluateReportInvalidData() {
        Long volunteerChatId = 123L;
        String messageText = "invalid_data";

        checkUserReportService.evaluateReport(volunteerChatId, messageText);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка evaluateReport с корректными данными и неизвестным животным")
    void testEvaluateReportUnknownPet() {
        Long volunteerChatId = 123L;
        String messageText = "4_unknown_1";

        checkUserReportService.evaluateReport(volunteerChatId, messageText);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Такого животного я пока не знаю.\n" + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка отчёта с корректными данными для оценки кошки")
    void testEvaluateReportCat() {
        Long volunteerChatId = 123L;
        String messageText = "4_cat_1";

        checkUserReportService.evaluateReport(volunteerChatId, messageText);

        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка отчёта с корректными данными для оценки собаки")
    void testEvaluateReportDog() {
        Long volunteerChatId = 123L;
        String messageText = "5_dog_2";

        checkUserReportService.evaluateReport(volunteerChatId, messageText);

        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("Проверка отчёта с некорректными числовыми данными")
    void testEvaluateReportInvalidNumberFormat() {
        Long volunteerChatId = 123L;
        String messageText = "invalid_number_format";

        checkUserReportService.evaluateReport(volunteerChatId, messageText);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка показа отчёта с существующим отчетом о собаке")
    void testViewDogReportWithExistingReport() {
        Long volunteerChatId = 123L;
        Long dogReportId = 1L;
        DogPhoto dogPhoto = new DogPhoto();
        dogPhoto.setData(new byte[]{0, 0, 0});
        DogReport dogReport = new DogReport();
        dogReport.setId(dogReportId);
        dogReport.setUpdated(LocalDateTime.now());
        dogReport.setRation("Хороший рацион");
        dogReport.setWellBeing("Отличное самочувствие");
        dogReport.setBehavior("Спокойное поведение");
        dogReport.setDogPhoto(dogPhoto);

        when(dogReportRepository.findById(dogReportId)).thenReturn(Optional.of(dogReport));

        checkUserReportService.viewDogReport(volunteerChatId, dogReportId);

        ArgumentCaptor<SendMessage> sendMessageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(4)).execute(sendMessageCaptor.capture());

        SendMessage sendMessage = sendMessageCaptor.getValue();
        assertEquals("После ознакомления с отчетом необходимо отправить его оценку по пятибальной шкале в формате \"\\оценка_dog_идентификатор\", где:\n" +
                "/5_dog_1 - отлично, можно составить подробное представление о жизни питомца на новом месте;\n" +
                "/4_dog_1 - хорошо, можно составить достаточное представление о жизни питомца на новом месте;\n" +
                "/3_dog_1 - удовлетворительно, лаконичная информация, по которой можно составить общее представление о жизни питомца на новом месте;\n" +
                "/2_dog_1 и /1_dog_1 - неудовлетворительно, усыновителю будет отправлено ПРЕДУПРЕЖДЕНИЕ о недобросовестном заполнении отчета.", sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }


    @Test
    @DisplayName("Проверка показа отчёта с отсутствующим отчетом о собаке")
    void testViewDogReportWithNonExistingReport() {
        Long volunteerChatId = 123L;
        Long dogReportId = 1L;

        when(dogReportRepository.findById(dogReportId)).thenReturn(Optional.empty());

        checkUserReportService.viewDogReport(volunteerChatId, dogReportId);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DOG_REPORT_NOT_FOUND_BY_ID_TEXT
                + dogReportId + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка показа отчёта с существующим отчетом о кошке")
    void testViewCatReportWithExistingReport() {
        Long volunteerChatId = 123L;
        Long catReportId = 1L;
        CatPhoto catPhoto = new CatPhoto();
        catPhoto.setData(new byte[]{0, 0, 0});
        CatReport catReport = new CatReport();
        catReport.setId(catReportId);
        catReport.setUpdated(LocalDateTime.now());
        catReport.setRation("Хороший рацион");
        catReport.setWellBeing("Отличное самочувствие");
        catReport.setBehavior("Спокойное поведение");
        catReport.setCatPhoto(catPhoto);

        when(catReportRepository.findById(catReportId)).thenReturn(Optional.of(catReport));

        checkUserReportService.viewCatReport(volunteerChatId, catReportId);

        ArgumentCaptor<SendMessage> sendMessageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(4)).execute(sendMessageCaptor.capture());

        SendMessage sendMessage = sendMessageCaptor.getValue();
        assertEquals("После ознакомления с отчетом необходимо отправить его оценку по пятибальной шкале в формате \"\\оценка_cat_идентификатор\", где:\n" +
                "/5_cat_1 - отлично, можно составить подробное представление о жизни питомца на новом месте;\n" +
                "/4_cat_1 - хорошо, можно составить достаточное представление о жизни питомца на новом месте;\n" +
                "/3_cat_1 - удовлетворительно, лаконичная информация, по которой можно составить общее представление о жизни питомца на новом месте;\n" +
                "/2_cat_1 и /1_cat_1 - неудовлетворительно, усыновителю будет отправлено ПРЕДУПРЕЖДЕНИЕ о недобросовестном заполнении отчета.", sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Проверка показа отчёта с отсутствующим отчетом о кошке")
    void testViewCatReportWithNonExistingReport() {
        Long volunteerChatId = 123L;
        Long catReportId = 1L;

        when(catReportRepository.findById(catReportId)).thenReturn(Optional.empty());

        checkUserReportService.viewCatReport(volunteerChatId, catReportId);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(CAT_REPORT_NOT_FOUND_BY_ID_TEXT
                + catReportId + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    //##################################################################################################################

    @Test
    @DisplayName("Просмотр отчета о собаке")
    void testViewDogReport() {
        Long volunteerChatId = 123L;
        String messageText = "/dog_1";

        checkUserReportService.viewReport(volunteerChatId, messageText);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DOG_REPORT_NOT_FOUND_BY_ID_TEXT
                + 1 + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Просмотр отчета о кошке")
    void testViewCatReport() {
        Long volunteerChatId = 123L;
        String messageText = "/cat_2"; // Пример текста сообщения

        checkUserReportService.viewReport(volunteerChatId, messageText);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(CAT_REPORT_NOT_FOUND_BY_ID_TEXT
                + 2 + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Ошибка из-за отсутствия данных")
    void testViewReportWithNoData() {
        Long volunteerChatId = 123L;
        String messageText = "";

        checkUserReportService.viewReport(volunteerChatId, messageText);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Ошибка из-за некорректного формата сообщения")
    void testViewReportWithInvalidFormat() {
        Long volunteerChatId = 123L;
        String messageText = "/invalid_data";

        checkUserReportService.viewReport(volunteerChatId, messageText);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Ошибка из-за некорректного ID")
    void testViewReportWithInvalidID() {
        Long volunteerChatId = 123L;
        String messageText = "/dog_invalidId"; // Некорректный ID

        checkUserReportService.viewReport(volunteerChatId, messageText);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals(DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    @Test
    @DisplayName("Ошибка из-за неизвестного животного")
    void testViewReportWithUnknownAnimal() {
        Long volunteerChatId = 123L;
        String messageText = "/unknown_1";

        checkUserReportService.viewReport(volunteerChatId, messageText);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(captor.capture());
        SendMessage sendMessage = captor.getValue();
        assertEquals("Такого животного я пока не знаю.\n" + DATA_IS_NOT_CORRECT_TEXT + WAY_BACK_TEXT, sendMessage.getParameters().get("text"));
        assertEquals(volunteerChatId, sendMessage.getParameters().get("chat_id"));
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка вызова метода init")
    void testInit() {
        Long chatId = 123L;

        checkUserReportService.init(chatId);

        verify(telegramBot, times(2)).execute(any(SendMessage.class));
        verify(checkUserReportStateService, times(1)).updateCheckUserReportState(chatId, ALL_REPORTS);
    }
}