package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.CheckUserReportState;

import java.util.Optional;

/**
 * Репозиторий для доступа к данным о состояниях проверки и оценки отчетов.
 */
@Repository
public interface CheckUserReportStateRepository extends JpaRepository<CheckUserReportState, Long> {
    /**
     * Находит состояние чата по идентификатору чата.
     *
     * @param chatId Идентификатор чата.
     * @return Optional, содержащий состояние проверки и оценки отчетов.
     */
    Optional<CheckUserReportState> findByChatId(Long chatId);
}
