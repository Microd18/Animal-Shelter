package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.UserReportState;

import java.util.Optional;

/**
 * Репозиторий для доступа к данным о состояниях отчёта юзера.
 */
@Repository
public interface UserReportStateRepository extends JpaRepository<UserReportState, Long> {

    /**
     * Находит состояние чата по идентификатору чата.
     *
     * @param chatId Идентификатор чата.
     * @return Optional, содержащий состояние отчёта юзера, если найдено, иначе пустой Optional.
     */
    Optional<UserReportState> findByChatId(Long chatId);
}
