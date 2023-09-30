package pro.sky.AnimalShelter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для проверки отчетов от усыновителей.
 * Этот сервис предоставляет методы для просмотра и оценки качества
 * заполнения отчетов от усыновителей кошек и собак.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CheckUserReportService {
}
