package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.Dog;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для доступа к данным о собаках.
 */
@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

    /**
     * Находит собаку по её идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Optional, содержащий чат, если найден, иначе пустой Optional.
     */
    Optional<Dog> findByUserId(Long chatId);

    /**
     * Отдает список айди юзеров, которые усыновили собак.
     *
     * @return List, содержащий user_id из таблицы собак. Если таких нет, то пустой List.
     */
    @Query(value = "SELECT dogs.user_id FROM dogs WHERE dogs.user_id IS NOT NULL", nativeQuery = true)
    List<Long> getDogAdopters();
}
