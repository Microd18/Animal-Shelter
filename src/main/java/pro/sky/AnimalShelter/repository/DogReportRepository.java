package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.DogReport;

import java.util.Optional;

/**
 * Репозиторий для доступа к данным об отчётах по собакам.
 */
@Repository
public interface DogReportRepository extends JpaRepository<DogReport, Long> {

    /**
     * Находит отчёт по его идентификатору.
     *
     * @param userId Идентификатор юзера.
     * @return Optional, содержащий чат, если найден, иначе пустой Optional.
     */
    Optional<DogReport> findByUserId(Long userId);
}
