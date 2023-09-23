package pro.sky.AnimalShelter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.entity.ChatState;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.ChatStateRepository;
import pro.sky.AnimalShelter.utils.JsonMapConverter;

import java.util.*;

import static pro.sky.AnimalShelter.enums.BotCommand.*;

/**
 * Сервис для управления очередью состояний чата.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatStateService {

    /**
     * Класс-конвертер для преобразования между JSON и объектами типа Map<Long, Deque<BotCommand>>.
     */
    private final JsonMapConverter jsonMapConverter;

    /**
     * Репозиторий для доступа к данным о состояниях чатов.
     */
    private final ChatStateRepository chatStateRepository;

    /**
     * Репозиторий для доступа к данным о чатах.
     */
    private final ChatRepository chatRepository;

    /**
     * Размер очереди состояний.
     */
    private static final int MAX_HISTORY_SIZE = 4;

    /**
     * Получает текущее состояние чата по его идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Текущее состояние чата или {@code STOP}, если чат не существует или бот не запущен.
     */
    public BotCommand getCurrentStateByChatId(Long chatId) {
        return chatRepository.findByChatId(chatId)
                .filter(Chat::isBotStarted)
                .flatMap(chat -> chatStateRepository.findByChatId(chat.getId()))
                .map(ChatState::getStateData)
                .map(jsonMapConverter::toMap)
                .map(map -> map.get(chatId))
                .flatMap(stateQueue -> stateQueue.isEmpty() ? Optional.of(STOP) : Optional.of(stateQueue.peek()))
                .orElse(STOP);
    }

    /**
     * Получает предыдущее состояние чата по его идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Предыдущее состояние чата или {@code STOP}, если чат не существует или бот не запущен.
     */
    public BotCommand getPreviousStateByChatId(Long chatId) {
        log.info("getPreviousStateByChatId method was invoked");
        return chatRepository.findByChatId(chatId)
                .filter(Chat::isBotStarted)
                .flatMap(chat -> chatStateRepository.findByChatId(chat.getId()))
                .map(ChatState::getStateData)
                .map(jsonMapConverter::toMap)
                .map(map -> map.get(chatId))
                .map(stateQueue -> {
                    if (!stateQueue.isEmpty()) {
                        Iterator<BotCommand> iterator = stateQueue.descendingIterator();
                        iterator.next();
                        if (iterator.hasNext()) {
                            return iterator.next();
                        }
                    }
                    return BotCommand.STOP;
                })
                .orElse(BotCommand.STOP);
    }

    /**
     * Получает последнее состояние чата по его идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Предыдущее состояние чата или {@code STOP}, если чат не существует или бот не запущен.
     */
    public BotCommand getLastStateByChatId(Long chatId) {
        log.info("getLastStateByChatId method was invoked");
        return chatRepository.findByChatId(chatId)
                .filter(Chat::isBotStarted)
                .flatMap(chat -> chatStateRepository.findByChatId(chat.getId()))
                .map(ChatState::getStateData)
                .map(jsonMapConverter::toMap)
                .map(map -> map.get(chatId))
                .flatMap(stateQueue -> stateQueue.isEmpty() ? Optional.of(STOP) : Optional.of(stateQueue.peekLast()))
                .orElse(STOP);
    }

    /**
     * Добавляет новое состояние в историю чата.
     *
     * @param chatId Идентификатор чата.
     * @param state  Состояние для добавления в историю.
     */
    public void updateChatState(Long chatId, BotCommand state) {
        log.info("updateChatState method was invoked");
        Chat chat = chatRepository.findByChatId(chatId)
                .orElseGet(() -> {
                    Chat newChat = new Chat();
                    newChat.setChatId(chatId);
                    newChat.setBotStarted(true);
                    return chatRepository.save(newChat);
                });

        chatStateRepository.findByChatId(chat.getId()).ifPresentOrElse(
                chatStateEntity -> {
                    Map<Long, Deque<BotCommand>> chatStateHistory = jsonMapConverter.toMap(chatStateEntity.getStateData());
                    Deque<BotCommand> stateStack = chatStateHistory.computeIfAbsent(chatId, k -> new LinkedList<>());

                    if (stateStack.size() >= MAX_HISTORY_SIZE) {
                        stateStack.pollLast();
                    }

                    if (state == BotCommand.START) {
                        chat.setBotStarted(true);
                        chatRepository.save(chat);
                    }

                    stateStack.push(state);

                    String stateDataJson = jsonMapConverter.toJson(chatStateHistory);
                    chatStateEntity.setStateData(stateDataJson);
                    chatStateRepository.save(chatStateEntity);
                },
                () -> {
                    Map<Long, Deque<BotCommand>> chatStateHistory = new HashMap<>();
                    Deque<BotCommand> stateStack = new LinkedList<>();
                    stateStack.push(state);
                    chatStateHistory.put(chatId, stateStack);

                    String stateDataJson = jsonMapConverter.toJson(chatStateHistory);
                    ChatState newChatState = new ChatState();
                    newChatState.setStateData(stateDataJson);
                    newChatState.setChat(chat);
                    chatStateRepository.save(newChatState);
                }
        );
    }

    /**
     * Останавливает бота в указанном чате.
     *
     * @param chatId Идентификатор чата.
     */
    public void stopBot(Long chatId) {
        log.info("stopBot method was invoked");
        chatRepository.findByChatId(chatId).ifPresent(foundChat ->
                chatStateRepository.findByChatId(foundChat.getId()).ifPresent(foundChatState -> {
                    foundChat.setBotStarted(false);
                    Map<Long, Deque<BotCommand>> chatStateHistory = new HashMap<>();
                    chatStateHistory.put(chatId, new LinkedList<>());
                    foundChatState.setStateData(jsonMapConverter.toJson(chatStateHistory));
                    chatRepository.save(foundChat);
                    chatStateRepository.save(foundChatState);
                }));
    }

    /**
     * Получает последнее состояние (команду), которая равна CAT или DOG для указанного чата.
     *
     * @param chatId идентификатор чата
     * @return последнее состояние CAT или DOG, или BotCommand.STOP, если не найдено
     */
    public BotCommand getLastStateCatOrDogByChatId(Long chatId) {
        log.info("getLastStateCatOrDogByChatId method was invoked");
        return chatRepository.findByChatId(chatId)
                .filter(Chat::isBotStarted)
                .flatMap(chat -> chatStateRepository.findByChatId(chat.getId()))
                .map(ChatState::getStateData)
                .map(jsonMapConverter::toMap)
                .map(map -> map.get(chatId))
                .map(stateQueue -> {
                    for (BotCommand command : stateQueue) {
                        if (CAT.equals(command) || DOG.equals(command)) {
                            return command;
                        }
                    }
                    return STOP;
                })
                .orElse(STOP);
    }

    /**
     * Переходит к предыдущему состоянию чата (удаляет текущее состояние) для указанного чата.
     *
     * @param chatId идентификатор чата
     */
    public void goToPreviousState(Long chatId) {
        log.info("goToPreviousState method was invoked");

        chatRepository.findByChatId(chatId)
                .filter(Chat::isBotStarted)
                .flatMap(chat -> chatStateRepository.findByChatId(chat.getId()))
                .ifPresent(chatStateEntity -> {
                    String stateData = chatStateEntity.getStateData();
                    if (stateData != null) {
                        Map<Long, Deque<BotCommand>> stateMap = new HashMap<>(jsonMapConverter.toMap(stateData));
                        Deque<BotCommand> stateQueue = stateMap.get(chatId);
                        if (!stateQueue.isEmpty()) {
                            stateQueue.pollFirst();
                            stateMap.put(chatId, stateQueue);
                            String updatedStateData = jsonMapConverter.toJson(stateMap);
                            chatStateEntity.setStateData(updatedStateData);
                            chatStateRepository.save(chatStateEntity);
                        } else {
                            log.error("State queue is empty");
                        }
                    } else {
                        log.error("StateData is null");
                    }
                });

    }
}
