package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.Dog;

import java.util.Optional;

/**
 * Репозиторий для доступа к данным о собаках.
 */
@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

    /**
     * Находит кошку по её идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Optional, содержащий чат, если найден, иначе пустой Optional.
     */
    Optional<Dog> findByUserId(Long chatId);
}
