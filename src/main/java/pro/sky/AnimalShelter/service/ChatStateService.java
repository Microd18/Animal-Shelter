package pro.sky.AnimalShelter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.entity.ChatState;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.ChatStateRepository;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

import static pro.sky.AnimalShelter.enums.BotCommand.*;

/**
 * Сервис для управления очередью состояний чата.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatStateService {

    /**
     * Репозиторий для доступа к данным о состояниях чатов.
     */
    private final ChatStateRepository chatStateRepository;

    /**
     * Репозиторий для доступа к данным о чатах.
     */
    private final ChatRepository chatRepository;

    /**
     * Сервис для управления состояниями отчётов.
     */
    private final UserReportStateService userReportStateService;

    /**
     * Размер очереди состояний.
     */
    private static final int MAX_HISTORY_CHAT_STATE_SIZE = 4;

    /**
     * Получает текущее состояние чата по его идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Текущее состояние чата или {@code STOP}, если чат не существует или бот не запущен.
     */
    public BotCommand getCurrentStateByChatId(Long chatId) {
        log.info("getCurrentStateByChatId method was invoked");
        return chatRepository.findByChatId(chatId)
                .filter(Chat::isBotStarted)
                .flatMap(chat -> chatStateRepository.findByChatId(chat.getId()))
                .map(ChatState::getStateData)
                .flatMap(stateQueue -> stateQueue.isEmpty() ? Optional.of(STOP) : Optional.of(stateQueue.peek()))
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
                    var stateStack = Optional.ofNullable(chatStateEntity.getStateData())
                            .orElseGet(LinkedList::new);
                    if (stateStack.size() >= MAX_HISTORY_CHAT_STATE_SIZE) {
                        stateStack.pollLast();
                    }
                    if (state == BotCommand.START) {
                        chat.setBotStarted(true);
                        chatRepository.save(chat);
                    }
                    stateStack.push(state);
                    chatStateEntity.setStateData(stateStack);
                    chatStateRepository.save(chatStateEntity);
                }, () -> {
                    Deque<BotCommand> stateStack = new LinkedList<>();
                    stateStack.push(state);

                    ChatState newChatState = new ChatState();
                    newChatState.setStateData(stateStack);
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
                chatStateRepository.findByChatId(foundChat.getId()).ifPresentOrElse(foundChatState -> {
                    foundChat.setBotStarted(false);
                    foundChatState.setStateData(new LinkedList<>());
                    chatRepository.save(foundChat);
                    chatStateRepository.save(foundChatState);
                    userReportStateService.clearUserReportStates(chatId);
                }, () -> foundChat.setBotStarted(false)));
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
                    var stateData = chatStateEntity.getStateData();
                    if (stateData != null) {
                        if (!stateData.isEmpty()) {
                            stateData.pollFirst();
                            chatStateEntity.setStateData(stateData);
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
