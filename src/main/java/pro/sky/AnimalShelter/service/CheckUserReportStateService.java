package pro.sky.AnimalShelter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.entity.CheckUserReportState;
import pro.sky.AnimalShelter.enums.CheckUserReportStates;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.CheckUserReportStateRepository;
import pro.sky.AnimalShelter.utils.CommonUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Сервис для управления очередью состояний проверки отчетов от усыновителей.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CheckUserReportStateService {

    /**
     * Репозиторий для доступа к данным о состоянии в меню проверки отчетов.
     */
    private final CheckUserReportStateRepository checkUserReportStateRepository;

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
    private static final int MAX_HISTORY_CHECK_USER_REPORT_STATE_SIZE = 3;

    /**
     * Получает текущее состояние отчёта по идентификатору чата.
     *
     * @param chatId Идентификатор чата.
     * @return Текущее состояние отчёта или {@code STOP}, если чат не существует или бот не запущен.
     */
    public CheckUserReportStates getCurrentStateByChatId(Long chatId) {
        log.info("getCurrentStateByChatId method was invoked");
        return chatRepository.findByChatId(chatId)
                .filter(Chat::isBotStarted)
                .flatMap(chat -> checkUserReportStateRepository.findByChatId(chat.getId()))
                .map(CheckUserReportState::getStateData)
                .flatMap(stateQueue -> stateQueue.isEmpty() ? Optional.empty() : Optional.of(stateQueue.peek()))
                .orElse(null);
    }

    /**
     * Добавляет новое состояние в историю отчёта.
     *
     * @param chatId Идентификатор чата.
     * @param state  Состояние для добавления в историю.
     */
    public void updateCheckUserReportState(Long chatId, CheckUserReportStates state) {
        log.info("updateCheckUserReportState method was invoked");
        Chat chat = chatRepository.findByChatId(chatId)
                .orElseGet(() -> {
                    commonUtils.offerToStart(chatId);
                    return null;
                });

        if (chat == null) {
            return;
        }

        checkUserReportStateRepository.findByChatId(chat.getId()).ifPresentOrElse(
                checkUserReportState -> {
                    var stateStack = Optional.ofNullable(checkUserReportState.getStateData())
                            .orElseGet(LinkedList::new);

                    if (stateStack.size() >= MAX_HISTORY_CHECK_USER_REPORT_STATE_SIZE) {
                        stateStack.pollLast();
                    }

                    stateStack.push(state);
                    checkUserReportState.setStateData(stateStack);
                    checkUserReportStateRepository.save(checkUserReportState);
                },
                () -> checkUserReportStateRepository.save(CheckUserReportState.builder()
                        .stateData(new LinkedList<>(Collections.singleton(state)))
                        .chat(chat)
                        .build()
                )
        );
    }
}
