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
import pro.sky.AnimalShelter.utils.JsonMapConverter;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    private JsonMapConverter jsonMapConverter;

    @InjectMocks
    private ChatStateService chatStateService;

    private Chat chat;

    private ChatState chatState;

    Long chatId;

    @BeforeEach
    public void setUp() {
        chatId = 1L;
        chat = new Chat();
        chat.setId(chatId);
        chatState = new ChatState();
        chatState.setChat(chat);
        chatState.setStateData("{}");

        lenient().when(chatStateRepository.findByChatId(anyLong())).thenReturn(Optional.empty());
        lenient().when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.of(chatState));
        lenient().when(chatRepository.findByChatId(chatId)).thenReturn(Optional.empty());
        lenient().when(chatRepository.findByChatId(chatId)).thenReturn(Optional.of(chat));
        lenient().when(jsonMapConverter.toCommandStatesJson(any())).thenReturn("{}");
        lenient().when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.empty());
        lenient().when(jsonMapConverter.toCommandStatesMap(chatState.getStateData())).thenReturn(Collections.singletonMap(chatId, new LinkedList<>(Arrays.asList(START, STOP))));
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния по ИД чата при выключенном боте")
    void testGetCurrentStateByChatId_BotNotStarted() {
        BotCommand result = chatStateService.getCurrentStateByChatId(chatId);
        assertEquals(STOP, result);
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния по ИД чата при включенном боте и отсутствии состояния чата")
    void testGetCurrentStateByChatId_BotStarted_NoChatState() {
        BotCommand result = chatStateService.getCurrentStateByChatId(chatId);
        assertEquals(STOP, result);
    }

    @Test
    @DisplayName("Проверка на получение текущего состояния по ИД чата при включенном боте и наличии состояния чата")
    void testGetCurrentStateByChatId_BotStarted_ChatStateExists() {
        chat.setBotStarted(true);
        when(chatStateRepository.findByChatId(anyLong())).thenReturn(Optional.of(chatState));
        BotCommand result = chatStateService.getCurrentStateByChatId(chatId);
        assertEquals(START, result);
    }

    @Test
    @DisplayName("Проверка на получение предыдущего состояния по ИД чата при выключенном боте")
    void testGetPreviousStateByChatId_BotNotStarted() {
        BotCommand result = chatStateService.getPreviousStateByChatId(chatId);
        assertEquals(STOP, result);
    }

    @Test
    @DisplayName("Проверка на получение предыдущего состояния по ИД чата при включенном боте и отсутствии состояния чата")
    void testGetPreviousStateByChatId_BotStarted_NoChatState() {
        BotCommand result = chatStateService.getPreviousStateByChatId(chatId);
        assertEquals(STOP, result);
    }

    @Test
    @DisplayName("Проверка на получение предыдущего состояния по ИД чата при включенном боте и наличии состояния чата")
    void testGetPreviousStateByChatId_BotStarted_ChatStateExists() {
           chat.setBotStarted(true);
        when(chatStateRepository.findByChatId(anyLong())).thenReturn(Optional.of(chatState));
        BotCommand result = chatStateService.getPreviousStateByChatId(chatId);
        assertEquals(START, result);
    }

    @Test
    @DisplayName("Проверка на получение последнего состояния по ИД чата при выключенном боте")
    void testGetLastStateByChatId_BotNotStarted() {
        BotCommand result = chatStateService.getLastStateByChatId(chatId);
        assertEquals(STOP, result);
    }

    @Test
    @DisplayName("Проверка на получение последнего состояния по ИД чата при включенном боте и отсутствии состояния чата")
    void testGetLastStateByChatId_BotStarted_NoChatState() {
        BotCommand result = chatStateService.getLastStateByChatId(chatId);
        assertEquals(STOP, result);
    }

    @Test
    @DisplayName("Проверка на получение последнего состояния по ИД чата при включенном боте и наличии состояния чата")
    void testGetLastStateByChatId_BotStarted_ChatStateExists() {
        BotCommand result = chatStateService.getLastStateByChatId(chatId);
        assertEquals(STOP, result);
    }

    @Test
    @DisplayName("Проверка на обновление состояния чата при отсутствии чата")
    void testUpdateChatState_ChatNotExists() {
        when(chatRepository.findByChatId(anyLong())).thenReturn(Optional.empty());
        when(chatRepository.save(any())).thenReturn(chat);

        chatStateService.updateChatState(chatId, START);

        verify(chatRepository, times(1)).save(any());
        verify(chatStateRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Проверка на обновление состояния чата при наличии чата и отсутствии состояния чата")
    void testUpdateChatState_ChatExists_NoChatState() {
        chatStateService.updateChatState(chatId, START);

        verify(chatRepository, never()).save(any());
        verify(chatStateRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Проверка на обновление состояния чата при наличии чата и состоянии чата")
    void testUpdateChatState_ChatExists_ChatStateExists() {
        chatStateService.updateChatState(chatId, BotCommand.CAT);

        verify(chatRepository, never()).save(any());
        verify(chatStateRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Проверка на остановку бота при отсутствии чата")
    void testStopBot_ChatNotExists() {
        chatStateService.stopBot(chatId);

        verify(chatRepository, never()).save(any());
        verify(chatStateRepository, never()).save(any());
    }

    @Test
    @DisplayName("Проверка на остановку бота при наличии чата и отсутствии состояния чата")
    void testStopBot_ChatExists_NoChatState() {
        chatStateService.stopBot(chatId);

        verify(chatRepository, never()).save(chat);
        verify(chatStateRepository, never()).save(any());

        assertFalse(chat.isBotStarted());
    }

    @Test
    @DisplayName("Проверка на остановку бота при наличии чата и состоянии чата")
    void testStopBot_ChatExists_ChatStateExists() {
        when(chatStateRepository.findByChatId(chat.getId())).thenReturn(Optional.of(chatState));
        chatStateService.stopBot(chatId);

        verify(chatRepository, times(1)).save(any());
        verify(chatStateRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Проверка на получение последнего состояния Cat или Dog по ИД чата при выключенном боте")
    void testGetLastStateCatOrDogByChatId_BotNotStarted() {
        BotCommand result = chatStateService.getLastStateCatOrDogByChatId(chatId);
        assertEquals(STOP, result);
    }

    @Test
    @DisplayName("Проверка на получение последнего состояния Cat или Dog по ИД чата при включенном боте и отсутствии состояния чата")
    void testGetLastStateCatOrDogByChatId_BotStarted_NoChatState() {
        BotCommand result = chatStateService.getLastStateCatOrDogByChatId(chatId);
        assertEquals(STOP, result);
    }

    @Test
    @DisplayName("Проверка на получение последнего состояния Cat или Dog по ИД чата при включенном боте и наличии состояния чата без Cat или Dog")
    void testGetLastStateCatOrDogByChatId_BotStarted_ChatStateExists_NoCatOrDog() {
        BotCommand result = chatStateService.getLastStateCatOrDogByChatId(chatId);
        assertEquals(STOP, result);
    }

    @Test
    @DisplayName("Проверка на получение последнего состояния Cat или Dog по ИД чата при включенном боте и наличии состояния чата с Cat или Dog")
    void testGetLastStateCatOrDogByChatId_BotStarted_ChatStateExists_CatOrDogFound() {
        chat.setBotStarted(true);
        when(chatStateRepository.findByChatId(anyLong())).thenReturn(Optional.of(chatState));
        when(jsonMapConverter.toCommandStatesMap(chatState.getStateData())).thenReturn(Collections.singletonMap(chatId, new LinkedList<>(Arrays.asList(START, BotCommand.CAT, BotCommand.DOG))));

        BotCommand result = chatStateService.getLastStateCatOrDogByChatId(chatId);
        assertEquals(CAT, result);
    }

    @Test
    @DisplayName("Проверка на переход к предыдущему состоянию при отсутствии чата")
    void testGoToPreviousState_ChatNotExists() {
        chatStateService.goToPreviousState(chatId);
        verify(chatStateRepository, never()).save(any());
    }

    @Test
    @DisplayName("Проверка на переход к предыдущему состоянию при наличии чата и отсутствии состояния чата")
    void testGoToPreviousState_ChatExists_NoChatState() {
        chatStateService.goToPreviousState(chatId);
        verify(chatStateRepository, never()).save(any());
    }

    @Test
    @DisplayName("Проверка на переход к предыдущему состоянию при наличии чата, состоянии чата и непустой очереди состояний")
    void testGoToPreviousState_ChatExists_ChatStateExists_StateQueueNotEmpty() {
        chat.setBotStarted(true);
        Deque<BotCommand> stateQueue = new LinkedList<>(Arrays.asList(START, STOP));
        when(jsonMapConverter.toCommandStatesMap(any())).thenReturn(Collections.singletonMap(chatId, stateQueue));
        when(chatStateRepository.findByChatId(anyLong())).thenReturn(Optional.of(chatState));

        chatStateService.goToPreviousState(chatId);

        verify(chatStateRepository, times(1)).save(any());
        verify(chatStateRepository, times(1)).findByChatId(any());
        verify(jsonMapConverter, times(1)).toCommandStatesMap(any());
        verify(jsonMapConverter, times(1)).toCommandStatesJson(any());
    }
}

