package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.CatReport;

import java.util.Optional;

/**
 * Репозиторий для доступа к данным об отчётах по кошкам.
 */
@Repository
public interface CatReportRepository extends JpaRepository<CatReport, Long> {

    /**
     * Находит отчёт по его идентификатору.
     *
     * @param userId Идентификатор юзера.
     * @return Optional, содержащий чат, если найден, иначе пустой Optional.
     */
    Optional<CatReport> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
