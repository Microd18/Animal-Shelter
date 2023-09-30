package pro.sky.AnimalShelter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.CheckUserReportStateRepository;
import pro.sky.AnimalShelter.utils.CommonUtils;
import pro.sky.AnimalShelter.utils.JsonMapConverter;

/**
 * Сервис для управления очередью состояний проверки отчетов от усыновителей.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CheckUserReportStateService {
    /**
     * Класс-конвертер для преобразования между JSON и объектами типа Map<Long, Deque<>>.
     */
    private final JsonMapConverter jsonMapConverter;

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

}
