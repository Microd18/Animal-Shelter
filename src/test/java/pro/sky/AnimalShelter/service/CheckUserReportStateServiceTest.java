package pro.sky.AnimalShelter.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.entity.CheckUserReportState;
import pro.sky.AnimalShelter.enums.CheckUserReportStates;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.CheckUserReportStateRepository;
import pro.sky.AnimalShelter.utils.CommonUtils;
import pro.sky.AnimalShelter.utils.ConsoleOutputCapture;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.CheckUserReportStates.*;

@ExtendWith(MockitoExtension.class)
class CheckUserReportStateServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private CheckUserReportStateRepository checkUserReportStateRepository;

    @Mock
    private CommonUtils commonUtils;

    @InjectMocks
    private CheckUserReportStateService checkUserReportStateService;

    private ConsoleOutputCapture outputCapture;

    private CheckUserReportState checkUserReportState;

    private Deque<CheckUserReportStates> stateQueue;

    private Chat chat;

    @BeforeEach
    public void setUp() {
        stateQueue = new LinkedList<>();
        checkUserReportState = new CheckUserReportState();
        outputCapture = new ConsoleOutputCapture();
        chat = new Chat();
        chat.setId(123L);
        lenient().when(chatRepository.findByChatId(123L)).thenReturn(Optional.of(chat));
    }

    @AfterEach
    public void cleanup() {
        outputCapture.stopCapture();
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния отчёта когда чат не найден")
    public void testGetCurrentStateByChatIdWhenChatNotFound() {
        when(chatRepository.findByChatId(123L)).thenReturn(Optional.empty());

        assertNull(checkUserReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(checkUserReportStateRepository, times(0)).findByChatId(any());
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния отчёта когда бот не запущен")
    public void testGetCurrentStateByChatIdWhenBotNotStarted() {
        chat.setBotStarted(false);

        assertNull(checkUserReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(checkUserReportStateRepository, times(0)).findByChatId(any());
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния отчёта когда отсутствует объект CheckUserReportState по chatId")
    public void testGetCurrentStateByChatIdWhenNoCheckUserReportState() {
        chat.setBotStarted(true);

        when(checkUserReportStateRepository.findByChatId(123L)).thenReturn(Optional.empty());

        assertNull(checkUserReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(checkUserReportStateRepository, times(1)).findByChatId(any());
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния отчёта когда у объекта CheckUserReportState данные состояний равны null")
    public void testGetCurrentStateByChatIdWhenStateDataIsNull() {
        chat.setBotStarted(true);
        checkUserReportState.setStateData(null);

        when(checkUserReportStateRepository.findByChatId(123L)).thenReturn(Optional.of(checkUserReportState));

        assertNull(checkUserReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(checkUserReportStateRepository, times(1)).findByChatId(any());
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния отчёта с валидной очередью состояний")
    public void testGetCurrentStateByChatIdWithValidStateQueue() {
        chat.setBotStarted(true);
        stateQueue.add(ALL_REPORTS);
        stateQueue.add(VIEW_REPORT);
        checkUserReportState.setStateData(stateQueue);


        when(checkUserReportStateRepository.findByChatId(anyLong())).thenReturn(Optional.of(checkUserReportState));

        assertEquals(ALL_REPORTS, checkUserReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(checkUserReportStateRepository, times(1)).findByChatId(any());
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния отчёта с пустой очередью состояний")
    public void testGetCurrentStateByChatIdWithEmptyStateQueue() {
        chat.setBotStarted(true);
        checkUserReportState.setStateData(stateQueue);

        when(checkUserReportStateRepository.findByChatId(anyLong())).thenReturn(Optional.of(checkUserReportState));

        assertNull(checkUserReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(checkUserReportStateRepository, times(1)).findByChatId(any());
    }

    //###############################################################################################################

    @Test
    @DisplayName("Проверка добавления нового состояния в историю отчёта с несуществующим чатом")
    public void testUpdateCheckUserReportStateWithNonExistentChat() {
        when(chatRepository.findByChatId(123L)).thenReturn(Optional.empty());

        checkUserReportStateService.updateCheckUserReportState(123L, ALL_REPORTS);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(commonUtils, times(1)).offerToStart(123L);
        verify(checkUserReportStateRepository, times(0)).findByChatId(any());
        verify(checkUserReportStateRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Проверка добавления нового состояния в историю отчёта без истории")
    public void testUpdateCheckUserReportStateWithoutHistory() {
        when(checkUserReportStateRepository.findByChatId(123L)).thenReturn(Optional.empty());

        checkUserReportStateService.updateCheckUserReportState(123L, ALL_REPORTS);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(checkUserReportStateRepository, times(1)).findByChatId(123L);
        verify(checkUserReportStateRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Проверка добавления нового состояния в историю отчёта с историей")
    public void testUpdateCheckUserReportStateWithHistory() {
        stateQueue.add(ALL_REPORTS);
        stateQueue.add(VIEW_REPORT);
        checkUserReportState.setStateData(stateQueue);


        when(checkUserReportStateRepository.findByChatId(123L)).thenReturn(Optional.of(checkUserReportState));

        checkUserReportStateService.updateCheckUserReportState(123L, EVALUATE_REPORT);

        assertEquals(stateQueue.getFirst(), EVALUATE_REPORT);
        assertEquals(stateQueue.getLast(), VIEW_REPORT);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(checkUserReportStateRepository, times(1)).findByChatId(123L);
        verify(checkUserReportStateRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Проверка добавления нового состояния в историю отчёта с историей, когда data равна null")
    public void testUpdateCheckUserReportStateWithHistoryDataIsNull() {
        chat.setBotStarted(true);
        checkUserReportState.setStateData(null);

        when(checkUserReportStateRepository.findByChatId(123L)).thenReturn(Optional.of(checkUserReportState));

        checkUserReportStateService.updateCheckUserReportState(123L, ALL_REPORTS);

        verify(chatRepository, times(1)).findByChatId(any());
        verify(checkUserReportStateRepository, times(1)).findByChatId(any());
        verify(checkUserReportStateRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Проверка добавления нового состояния в историю отчёта с максимальным размером")
    public void testUpdateCheckUserReportStateWithMaxHistorySize() {
        chat.setBotStarted(true);
        stateQueue.add(ALL_REPORTS);
        stateQueue.add(EVALUATE_REPORT);
        stateQueue.add(VIEW_REPORT);
        checkUserReportState.setStateData(stateQueue);

        when(checkUserReportStateRepository.findByChatId(123L)).thenReturn(Optional.of(checkUserReportState));

        checkUserReportStateService.updateCheckUserReportState(123L, VIEW_REPORT);

        assertEquals(stateQueue.size(), 3);
        assertEquals(stateQueue.getLast(), EVALUATE_REPORT);
        assertEquals(stateQueue.getFirst(), VIEW_REPORT);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(checkUserReportStateRepository, times(1)).findByChatId(123L);
        verify(checkUserReportStateRepository, times(1)).save(any());
    }
}