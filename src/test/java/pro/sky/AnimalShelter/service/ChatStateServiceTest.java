package pro.sky.AnimalShelter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.entity.ChatState;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.ChatStateRepository;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pro.sky.AnimalShelter.enums.BotCommand.*;

@ExtendWith(MockitoExtension.class)
public class ChatStateServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatStateRepository chatStateRepository;

    @Mock
    private UserReportStateService userReportStateService;

    @InjectMocks
    private ChatStateService chatStateService;

    private Deque<BotCommand> stateQueue;

    private Chat chat;

    private ChatState chatState;

    private Long chatId;

    @BeforeEach
    public void setUp() {
        stateQueue = new LinkedList<>();
        chatId = 123L;
        chat = new Chat();
        chat.setId(chatId);
        chatState = new ChatState();
        chatState.setChat(chat);

        lenient().when(chatRepository.findByChatId(chatId)).thenReturn(Optional.of(chat));
    }


    @Test
    @DisplayName("Проверка на получение текущего состояния чата с валидной очередью состояний")
    public void testGetCurrentStateByChatIdWithValidStateQueue() {
        chat.setBotStarted(true);
        stateQueue.add(DOG);
        stateQueue.add(START);
        chatState.setStateData(stateQueue);


        when(chatStateRepository.findByChatId(chatId)).thenReturn(Optional.of(chatState));

        assertEquals(DOG, chatStateService.getCurrentStateByChatId(chatId));

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния чата когда чат не найден")
    public void testGetCurrentStateByChatIdWhenChatNotFound() {
        when(chatRepository.findByChatId(chatId)).thenReturn(Optional.empty());

        assertEquals(STOP, chatStateService.getCurrentStateByChatId(chatId));

        verify(chatRepository, times(1)).findByChatId(any());
        verifyNoInteractions(chatStateRepository);
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния чата когда бот не запущен")
    public void testGetCurrentStateByChatIdWhenBotNotStarted() {
        chat.setBotStarted(false);

        assertEquals(STOP, chatStateService.getCurrentStateByChatId(chatId));

        verify(chatRepository, times(1)).findByChatId(any());
        verifyNoInteractions(chatStateRepository);
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния чата когда отсутствует объект ChatState по chatId")
    public void testGetCurrentStateByChatIdWhenNoUserReportState() {
        chat.setBotStarted(true);

        when(chatStateRepository.findByChatId(chatId)).thenReturn(Optional.empty());

        assertEquals(STOP, chatStateService.getCurrentStateByChatId(chatId));

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния чата когда у объекта ChatState данные состояний равны null")
    public void testGetCurrentStateByChatIdWhenStateDataIsNull() {
        chat.setBotStarted(true);
        chatState.setStateData(null);

        when(chatStateRepository.findByChatId(chatId)).thenReturn(Optional.of(chatState));

        assertEquals(STOP, chatStateService.getCurrentStateByChatId(chatId));

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
    }


    @Test
    @DisplayName("Проверка на получение текущего состояния чата с пустой очередью состояний")
    public void testGetCurrentStateByChatIdWithEmptyStateQueue() {
        chat.setBotStarted(true);
        chatState.setStateData(stateQueue);

        when(chatStateRepository.findByChatId(chatId)).thenReturn(Optional.of(chatState));

        assertEquals(STOP, chatStateService.getCurrentStateByChatId(chatId));

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка на добавление нового состояния в историю чата")
    public void testUpdateChatStateAddNewState() {
        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.empty());

        chatStateService.updateChatState(chatId, START);

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).save(any(ChatState.class));
        verify(chatRepository, times(0)).save(chat);
    }

    @Test
    @DisplayName("Проверка на добавление нового состояния в историю чата, когда чат не существует")
    public void testUpdateChatStateAddNewStateExistingChat() {
        when(chatRepository.findByChatId(chatId)).thenReturn(Optional.empty());
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);
        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.empty());

        chatStateService.updateChatState(chatId, START);

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).save(any(ChatState.class));
        verify(chatRepository, times(1)).save(any(Chat.class));
        verify(chatRepository, times(0)).save(chat);
    }

    @Test
    @DisplayName("Проверка на обновление существующего состояния в истории чата")
    public void testUpdateChatStateUpdateExistingState() {
        chatState.setStateData(stateQueue);


        when(chatStateRepository.findByChatId(chatId)).thenReturn(Optional.of(chatState));

        chatStateService.updateChatState(chatId, START);

        assertEquals(stateQueue.getLast(), START);
        assertEquals(stateQueue.getFirst(), START);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateRepository, times(1)).findByChatId(123L);
        verify(chatStateRepository, times(1)).save(any(ChatState.class));
        verify(chatRepository, times(1)).save(chat);
    }

    @Test
    @DisplayName("Проверка на обновление существующего состояния в истории чата, когда чат не найден")
    public void testUpdateChatStateUpdateExistingStateExistingChat() {
        chatState.setStateData(stateQueue);


        when(chatRepository.findByChatId(chatId)).thenReturn(Optional.empty());
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);
        when(chatStateRepository.findByChatId(chatId)).thenReturn(Optional.of(chatState));

        chatStateService.updateChatState(chatId, START);

        assertEquals(stateQueue.getLast(), START);
        assertEquals(stateQueue.getFirst(), START);

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).save(any(ChatState.class));
        verify(chatRepository, times(2)).save(any(Chat.class));
    }

    @Test
    @DisplayName("Проверка на обновление существующего состояния в истории чата с максимальным размером")
    public void testUpdateChatStateUpdateExistingStateWithMaxHistorySize() {
        stateQueue.add(START);
        stateQueue.add(DOG);
        stateQueue.add(PET_REPORT);
        stateQueue.add(SEND_REPORT);
        chatState.setStateData(stateQueue);


        when(chatStateRepository.findByChatId(chatId)).thenReturn(Optional.of(chatState));

        chatStateService.updateChatState(chatId, SEND_REPORT);

        assertEquals(stateQueue.getLast(), PET_REPORT);
        assertEquals(stateQueue.getFirst(), SEND_REPORT);

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).save(any(ChatState.class));
        verify(chatRepository, times(0)).save(any(Chat.class));
    }

    @Test
    @DisplayName("Проверка на обновление существующего состояния в истории чата, когда дата состояний равна null")
    public void testUpdateChatStateUpdateExistingStateDataIsNull() {
        chatState.setStateData(null);

        when(chatStateRepository.findByChatId(chatId)).thenReturn(Optional.of(chatState));

        chatStateService.updateChatState(chatId, START);

        verify(chatRepository, times(1)).findByChatId(123L);
        verify(chatStateRepository, times(1)).findByChatId(123L);
        verify(chatStateRepository, times(1)).save(any(ChatState.class));
        verify(chatRepository, times(1)).save(chat);
    }

    //##################################################################################################################

    @Test
    @DisplayName("Позитивный сценарий: остановка бота в чате")
    public void testStopBot() {
        chat.setBotStarted(true);
        stateQueue.add(START);
        stateQueue.add(DOG);
        chatState.setStateData(stateQueue);

        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.of(chatState));

        chatStateService.stopBot(chatId);

        assertFalse(chat.isBotStarted());

        ChatState updatedState = chatStateRepository.findByChatId(chat.getId()).orElse(null);
        assertNotNull(updatedState);
        Deque<BotCommand> chatStateHistory = updatedState.getStateData();
        assertTrue(chatStateHistory.isEmpty());

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatRepository, times(1)).save(chat);
        verify(chatStateRepository, times(2)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).save(chatState);
        verify(userReportStateService, times(1)).clearUserReportStates(chatId);
    }

    @Test
    @DisplayName("Проверка на остановку бота при отсутствии чата")
    void testStopBotChatNotExists() {
        when(chatRepository.findByChatId(chatId)).thenReturn(Optional.empty());

        chatStateService.stopBot(chatId);

        verify(chatRepository, never()).save(any());
        verify(chatRepository, times(1)).findByChatId(chatId);
        verifyNoInteractions(chatStateRepository, userReportStateService);
    }

    @Test
    @DisplayName("Проверка на остановку бота при наличии чата и отсутствии состояния чата")
    void testStopBotChatExistsNoChatState() {
        chat.setBotStarted(true);
        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.empty());

        chatStateService.stopBot(chatId);

        verify(chatRepository, never()).save(chat);
        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
        verifyNoInteractions(userReportStateService);

        assertFalse(chat.isBotStarted());
    }


    //##################################################################################################################

    @Test
    @DisplayName("Проверка на получение последнего состояния кошки в чате")
    public void testGetLastStateCatOrDogByChatIdCat() {
        chat.setBotStarted(true);
        stateQueue.add(START);
        stateQueue.add(CAT);
        stateQueue.add(DOG);
        stateQueue.add(SHELTER_INFO);
        chatState.setStateData(stateQueue);

        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.of(chatState));

        BotCommand lastState = chatStateService.getLastStateCatOrDogByChatId(chatId);

        assertEquals(CAT, lastState);

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
    }

    @Test
    @DisplayName("Проверка на получение последнего состояния собаки в чате")
    public void testGetLastStateCatOrDogByChatIdDog() {
        chat.setBotStarted(true);
        stateQueue.add(START);
        stateQueue.add(DOG);
        stateQueue.add(CAT);
        stateQueue.add(SHELTER_INFO);
        chatState.setStateData(stateQueue);

        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.of(chatState));

        BotCommand lastState = chatStateService.getLastStateCatOrDogByChatId(chatId);

        assertEquals(DOG, lastState);

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
    }

    @Test
    @DisplayName("Проверка на получение состояния STOP в чате без кошки и собаки")
    public void testGetLastStateCatOrDogByChatIdStopWithoutCatAndDog() {
        chat.setBotStarted(true);
        stateQueue.add(START);
        stateQueue.add(SHELTER_INFO);
        chatState.setStateData(stateQueue);

        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.of(chatState));

        BotCommand lastState = chatStateService.getLastStateCatOrDogByChatId(chatId);

        assertEquals(BotCommand.STOP, lastState);

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
    }

    @Test
    @DisplayName("Проверка на получение состояния STOP в чате, когда история состояний null")
    public void testGetLastStateCatOrDogByChatIdHistoryNull() {
        chat.setBotStarted(true);
        chatState.setStateData(null);

        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.of(chatState));

        BotCommand lastState = chatStateService.getLastStateCatOrDogByChatId(chatId);

        assertEquals(BotCommand.STOP, lastState);
    }

    @Test
    @DisplayName("Проверка на получение состояния STOP в чате, когда история состояний пустая")
    public void testGetLastStateCatOrDogByChatIdStateHistoryEmpty() {
        chat.setBotStarted(true);
        chatState.setStateData(new LinkedList<>());

        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.of(chatState));

        BotCommand lastState = chatStateService.getLastStateCatOrDogByChatId(chatId);

        assertEquals(BotCommand.STOP, lastState);
    }

    @Test
    @DisplayName("Проверка на получение последнего состояния собаки или кошки, когда чат не найден")
    public void testGetLastStateCatOrDogByChatIdChatNotFound() {
        when(chatRepository.findByChatId(chatId)).thenReturn(Optional.empty());

        BotCommand lastState = chatStateService.getLastStateCatOrDogByChatId(chatId);

        assertEquals(BotCommand.STOP, lastState);

        verifyNoInteractions(chatStateRepository);
    }


    @Test
    @DisplayName("Проверка на получение последнего состояния собаки или кошки, когда бот не запущен")
    public void testGetLastStateCatOrDogByChatIdBotNotStarted() {
        chat.setBotStarted(false);

        BotCommand lastState = chatStateService.getLastStateCatOrDogByChatId(chatId);

        assertEquals(BotCommand.STOP, lastState);

        verifyNoInteractions(chatStateRepository);
    }

    //##################################################################################################################

    @Test
    @DisplayName("Проверка на переход к предыдущему состоянию при наличии чата, состоянии чата и непустой очереди состояний")
    public void testGoToPreviousState() {
        chat.setBotStarted(true);
        stateQueue.add(CAT);
        stateQueue.add(START);
        chatState.setStateData(stateQueue);

        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.of(chatState));

        chatStateService.goToPreviousState(chatId);

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).save(chatState);
        verify(chatStateRepository, times(1)).findByChatId(chatId);

        assertEquals(1, stateQueue.size());
        assertEquals(START, stateQueue.pollFirst());
    }

    @Test
    @DisplayName("Проверка на переход к предыдущему состоянию, когда чат не найден")
    public void testGoToPreviousStateChatNotFound() {
        when(chatRepository.findByChatId(chatId)).thenReturn(Optional.empty());

        chatStateService.goToPreviousState(chatId);

        verify(chatRepository, times(1)).findByChatId(chatId);
        verifyNoInteractions(chatStateRepository);
    }

    @Test
    @DisplayName("Проверка на переход к предыдущему состоянию, когда бот не запущен")
    public void testGoToPreviousStateBotNotStarted() {
        chat.setBotStarted(false);

        chatStateService.goToPreviousState(chatId);

        verify(chatRepository, times(1)).findByChatId(chatId);
        verifyNoInteractions(chatStateRepository);
    }

    @Test
    @DisplayName("Проверка на переход к предыдущему состоянию, когда история состояний пустая")
    public void testGoToPreviousStateNoHistory() {
        chat.setBotStarted(true);
        chatState.setStateData(stateQueue);

        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.of(chatState));

        chatStateService.goToPreviousState(chatId);

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, never()).save(any());
    }

    @Test
    @DisplayName("Проверка на переход к предыдущему состоянию, когда история состояний null")
    public void testGoToPreviousStateStateDataIsNull() {
        chat.setBotStarted(true);
        chatState.setStateData(null);

        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.of(chatState));

        chatStateService.goToPreviousState(chatId);

        verify(chatRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, times(1)).findByChatId(chatId);
        verify(chatStateRepository, never()).save(any());
    }
}

