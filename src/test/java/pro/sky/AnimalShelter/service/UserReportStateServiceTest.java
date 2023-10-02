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
import pro.sky.AnimalShelter.entity.UserReportState;
import pro.sky.AnimalShelter.enums.UserReportStates;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.UserReportStateRepository;
import pro.sky.AnimalShelter.utils.CommonUtils;
import pro.sky.AnimalShelter.utils.ConsoleOutputCapture;
import pro.sky.AnimalShelter.utils.JsonMapConverter;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.UserReportStates.*;

@ExtendWith(MockitoExtension.class)
public class UserReportStateServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserReportStateRepository userReportStateRepository;

    @Mock
    private JsonMapConverter jsonMapConverter;

    @Mock
    private CommonUtils commonUtils;

    @InjectMocks
    private UserReportStateService userReportStateService;

    private ConsoleOutputCapture outputCapture;

    private Chat chat;

    @BeforeEach
    public void setUp() {
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

        assertNull(userReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(userReportStateRepository, times(0)).findByChatId(any());
        verify(jsonMapConverter, times(0)).toUserReportStatesMap(any());
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния отчёта когда бот не запущен")
    public void testGetCurrentStateByChatIdWhenBotNotStarted() {
        chat.setBotStarted(false);

        assertNull(userReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(userReportStateRepository, times(0)).findByChatId(any());
        verify(jsonMapConverter, times(0)).toUserReportStatesMap(any());
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния отчёта когда отсутствует объект UserReportState по chatId")
    public void testGetCurrentStateByChatIdWhenNoUserReportState() {
        chat.setBotStarted(true);

        when(userReportStateRepository.findByChatId(123L)).thenReturn(Optional.empty());

        assertNull(userReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(userReportStateRepository, times(1)).findByChatId(any());
        verify(jsonMapConverter, times(0)).toUserReportStatesMap(any());
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния отчёта когда у объекта UserReportState данные состояний равны null")
    public void testGetCurrentStateByChatIdWhenStateDataIsNull() {
        chat.setBotStarted(true);
        UserReportState userReportState = new UserReportState();
        userReportState.setStateData(null);

        when(userReportStateRepository.findByChatId(123L)).thenReturn(Optional.of(userReportState));

        assertNull(userReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(userReportStateRepository, times(1)).findByChatId(any());
        verify(jsonMapConverter, times(0)).toUserReportStatesMap(any());
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния отчёта с невалидными данными состояний")
    public void testGetCurrentStateByChatIdWhenInvalidStateData() {
        chat.setBotStarted(true);
        UserReportState userReportState = new UserReportState();
        userReportState.setStateData("invalid_json_data");

        when(userReportStateRepository.findByChatId(123L)).thenReturn(Optional.of(userReportState));

        assertNull(userReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(userReportStateRepository, times(1)).findByChatId(any());
        verify(jsonMapConverter, times(1)).toUserReportStatesMap("invalid_json_data");
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния отчёта с валидной очередью состояний")
    public void testGetCurrentStateByChatIdWithValidStateQueue() {
        chat.setBotStarted(true);
        UserReportState userReportState = new UserReportState();
        userReportState.setStateData("{\"123\": [\"PHOTO\", \"RATION\"]}");

        Map<Long, Deque<UserReportStates>> stateMap = new HashMap<>();
        Deque<UserReportStates> stateQueue = new LinkedList<>();
        stateQueue.add(UserReportStates.PHOTO);
        stateQueue.add(UserReportStates.RATION);
        stateMap.put(123L, stateQueue);

        when(userReportStateRepository.findByChatId(anyLong())).thenReturn(Optional.of(userReportState));
        when(jsonMapConverter.toUserReportStatesMap(anyString())).thenReturn(stateMap);

        assertEquals(UserReportStates.PHOTO, userReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(userReportStateRepository, times(1)).findByChatId(any());
        verify(jsonMapConverter, times(1)).toUserReportStatesMap(any());
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния отчёта с пустой очередью состояний")
    public void testGetCurrentStateByChatIdWithEmptyStateQueue() {
        chat.setBotStarted(true);
        UserReportState userReportState = new UserReportState();
        userReportState.setStateData("{\"123\": []}");

        Map<Long, Deque<UserReportStates>> stateMap = new HashMap<>();
        Deque<UserReportStates> stateQueue = new LinkedList<>();
        stateMap.put(123L, stateQueue);

        when(userReportStateRepository.findByChatId(anyLong())).thenReturn(Optional.of(userReportState));
        when(jsonMapConverter.toUserReportStatesMap(anyString())).thenReturn(stateMap);

        assertNull(userReportStateService.getCurrentStateByChatId(123L));

        verify(chatRepository, times(1)).findByChatId(any());
        verify(userReportStateRepository, times(1)).findByChatId(any());
        verify(jsonMapConverter, times(1)).toUserReportStatesMap(any());
    }

    //###############################################################################################################

    @Test
    @DisplayName("Проверка на очистку состояний отчёта при отсутствии чата")
    public void testClearUserReportStatesWhenChatNotFound() {
        when(chatRepository.findByChatId(123L)).thenReturn(Optional.empty());

        userReportStateService.clearUserReportStates(123L);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(0)).findByChatId(any());
        verify(userReportStateRepository, times(0)).save(any());
        verify(jsonMapConverter, times(0)).toUserReportStatesJson(any());
    }

    @Test
    @DisplayName("Проверка на очистку состояний отчёта при отсутствии UserReportState")
    public void testClearUserReportStatesWhenNoUserReportState() {
        when(userReportStateRepository.findByChatId(123L)).thenReturn(Optional.empty());

        userReportStateService.clearUserReportStates(123L);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(0)).save(any());
        verify(jsonMapConverter, times(0)).toUserReportStatesJson(any());
    }

    @Test
    @DisplayName("Проверка на успешную очистку состояний отчёта")
    public void testClearUserReportStatesSuccess() {
        UserReportState userReportState = new UserReportState();
        userReportState.setStateData("{\"123\": [\"PHOTO\", \"RATION\"]}");

        when(userReportStateRepository.findByChatId(123L)).thenReturn(Optional.of(userReportState));

        userReportStateService.clearUserReportStates(123L);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(1)).save(userReportState);
        verify(jsonMapConverter, times(1)).toUserReportStatesJson(any());
    }

    //###############################################################################################################

    @Test
    @DisplayName("Проверка добавления нового состояния в историю отчёта с несуществующим чатом")
    public void testUpdateUserReportStateWithNonExistentChat() {
        when(chatRepository.findByChatId(123L)).thenReturn(Optional.empty());

        userReportStateService.updateUserReportState(123L, BEHAVIOR);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(commonUtils, times(1)).offerToStart(123L);
        verify(userReportStateRepository, times(0)).findByChatId(any());
        verify(userReportStateRepository, times(0)).save(any());
        verify(jsonMapConverter, times(0)).toUserReportStatesJson(any());
    }

    @Test
    @DisplayName("Проверка добавления нового состояния в историю отчёта без истории")
    public void testUpdateUserReportStateWithoutHistory() {
        when(userReportStateRepository.findByChatId(123L)).thenReturn(Optional.empty());

        userReportStateService.updateUserReportState(123L, BEHAVIOR);


        verify(chatRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(1)).save(any());
        verify(jsonMapConverter, times(1)).toUserReportStatesJson(any());
    }

    @Test
    @DisplayName("Проверка добавления нового состояния в историю отчёта с историей")
    public void testUpdateUserReportStateWithHistory() {
        UserReportState userReportState = new UserReportState();
        userReportState.setStateData("{\"123\": [\"PHOTO\", \"RATION\"]}");
        Map<Long, Deque<UserReportStates>> stateMap = new HashMap<>();
        Deque<UserReportStates> stateQueue = new LinkedList<>();
        stateQueue.add(RATION);
        stateQueue.add(PHOTO);
        stateMap.put(123L, stateQueue);

        when(userReportStateRepository.findByChatId(123L)).thenReturn(Optional.of(userReportState));
        when(jsonMapConverter.toUserReportStatesMap("{\"123\": [\"PHOTO\", \"RATION\"]}")).thenReturn(stateMap);

        userReportStateService.updateUserReportState(123L, BEHAVIOR);

        assertEquals(stateQueue.getFirst(), BEHAVIOR);
        assertEquals(stateQueue.getLast(), PHOTO);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(1)).save(any());
        verify(jsonMapConverter, times(1)).toUserReportStatesJson(stateMap);
        verify(jsonMapConverter, times(1)).toUserReportStatesMap("{\"123\": [\"PHOTO\", \"RATION\"]}");
    }

    @Test
    @DisplayName("Проверка добавления нового состояния в историю отчёта с историей, когда data равна null")
    public void testUpdateUserReportStateWithHistoryDataIsNull() {
        UserReportState userReportState = new UserReportState();
        userReportState.setStateData(null);
        userReportState.setChat(chat);

        when(userReportStateRepository.findByChatId(123L)).thenReturn(Optional.of(userReportState));

        userReportStateService.updateUserReportState(123L, BEHAVIOR);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(1)).save(any());
        verify(jsonMapConverter, times(1)).toUserReportStatesJson(anyMap());
        verify(jsonMapConverter, times(1)).toUserReportStatesMap(null);
    }

    @Test
    @DisplayName("Проверка добавления нового состояния в историю отчёта с максимальным размером")
    public void testUpdateUserReportStateWithMaxHistorySize() {
        UserReportState userReportState = new UserReportState();
        userReportState.setStateData("{\"123\": [\"WELL_BEING\", \"BEHAVIOR\", \"PHOTO\", \"RATION\"]}");
        Map<Long, Deque<UserReportStates>> stateMap = new HashMap<>();
        Deque<UserReportStates> stateQueue = new LinkedList<>();
        stateQueue.add(RATION);
        stateQueue.add(PHOTO);
        stateQueue.add(BEHAVIOR);
        stateQueue.add(WELL_BEING);
        stateMap.put(123L, stateQueue);

        when(userReportStateRepository.findByChatId(123L)).thenReturn(Optional.of(userReportState));
        when(jsonMapConverter.toUserReportStatesMap("{\"123\": [\"WELL_BEING\", \"BEHAVIOR\", \"PHOTO\", \"RATION\"]}")).thenReturn(stateMap);

        userReportStateService.updateUserReportState(123L, WELL_BEING);

        assertEquals(stateQueue.size(), 4);
        assertEquals(stateQueue.getLast(), BEHAVIOR);
        assertEquals(stateQueue.getFirst(), WELL_BEING);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(1)).findByChatId(123L);
        verify(userReportStateRepository, times(1)).save(any());
        verify(jsonMapConverter, times(1)).toUserReportStatesJson(stateMap);
        verify(jsonMapConverter, times(1)).toUserReportStatesMap("{\"123\": [\"WELL_BEING\", \"BEHAVIOR\", \"PHOTO\", \"RATION\"]}");
    }
}
