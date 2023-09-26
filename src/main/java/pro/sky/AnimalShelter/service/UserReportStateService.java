package pro.sky.AnimalShelter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.entity.UserReportState;
import pro.sky.AnimalShelter.enums.UserReportStates;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.UserReportStateRepository;
import pro.sky.AnimalShelter.utils.CommonUtils;
import pro.sky.AnimalShelter.utils.JsonMapConverter;

import java.util.*;

/**
 * Сервис для управления очередью состояний отчёта юзера.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserReportStateService {

    /**
     * Класс-конвертер для преобразования между JSON и объектами типа Map<Long, Deque<>>.
     */
    private final JsonMapConverter<UserReportStates> jsonMapConverter = new JsonMapConverter<>(UserReportStates.class);

    /**
     * Репозиторий для доступа к данным о состояниях чатов.
     */
    private final UserReportStateRepository userReportStateRepository;

    /**
     * Репозиторий для доступа к данным о чатах.
     */
    private final ChatRepository chatRepository;

    /**
     * Экземпляр утилитарного класс для общих методов.
     */
    private final CommonUtils commonUtils;

    /**
     * Размер очереди состояний.
     */
    private static final int MAX_HISTORY_USER_REPORT_STATE_SIZE = 4;

    /**
     * Получает текущее состояние отчёта по идентификатору чата.
     *
     * @param chatId Идентификатор чата.
     * @return Текущее состояние отчёта или {@code STOP}, если чат не существует или бот не запущен.
     */
    public UserReportStates getCurrentStateByChatId(Long chatId) {
        log.info("getCurrentStateByChatId method was invoked");
        return chatRepository.findByChatId(chatId)
                .filter(Chat::isBotStarted)
                .flatMap(chat -> userReportStateRepository.findByChatId(chat.getId()))
                .map(UserReportState::getStateData)
                .map(jsonMapConverter::toMap)
                .map(map -> map.get(chatId))
                .flatMap(stateQueue -> stateQueue.isEmpty() ? Optional.empty() : Optional.of(stateQueue.peek()))
                .orElse(null);
    }

    /**
     * Добавляет новое состояние в историю отчёта.
     *
     * @param chatId Идентификатор чата.
     * @param state  Состояние для добавления в историю.
     */
    public void updateUserReportState(Long chatId, UserReportStates state) {
        log.info("updateUserReportState method was invoked");
        Chat chat = chatRepository.findByChatId(chatId)
                .orElseGet(() -> {
                    commonUtils.offerToStart(chatId);
                    return null;
                });

        if (chat == null) {
            return;
        }

        userReportStateRepository.findByChatId(chat.getId()).ifPresentOrElse(
                userReportState -> {
                    Map<Long, Deque<UserReportStates>> userReportStateHistory = jsonMapConverter.toMap(userReportState.getStateData());
                    Deque<UserReportStates> stateStack = userReportStateHistory.computeIfAbsent(chatId, k -> new LinkedList<>());

                    if (stateStack.size() >= MAX_HISTORY_USER_REPORT_STATE_SIZE) {
                        stateStack.pollLast();
                    }

                    stateStack.push(state);
                    String stateDataJson = jsonMapConverter.toJson(userReportStateHistory);
                    userReportState.setStateData(stateDataJson);
                    userReportStateRepository.save(userReportState);
                },
                () -> userReportStateRepository.save(UserReportState.builder()
                        .stateData(jsonMapConverter.toJson(
                                Collections.singletonMap(chatId, new LinkedList<>(Collections.singleton(state))))
                        )
                        .chat(chat)
                        .build()
                )
        );
    }

    /**
     * Очищает историю состояний отчёта пользователя в указанном чате.
     *
     * @param chatId Идентификатор чата.
     */
    public void clearUserReportStates(Long chatId) {
        log.info("clearUserReportStates method was invoked");
        chatRepository.findByChatId(chatId).flatMap(foundChat ->
                userReportStateRepository.findByChatId(foundChat.getId())).ifPresent(foundUserReportState -> {
            Map<Long, Deque<UserReportStates>> userReportStatesHistory = new HashMap<>();
            userReportStatesHistory.put(chatId, new LinkedList<>());
            foundUserReportState.setStateData(jsonMapConverter.toJson(userReportStatesHistory));
            userReportStateRepository.save(foundUserReportState);
        });
    }
}
