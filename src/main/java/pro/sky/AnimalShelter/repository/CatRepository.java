package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.Cat;

import java.util.Optional;

/**
 * Репозиторий для доступа к данным о кошках.
 */
@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {

    /**
     * Находит кошку по её идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Optional, содержащий чат, если найден, иначе пустой Optional.
     */
    Optional<Cat> findByUserId(Long chatId);
}
