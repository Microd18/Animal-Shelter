package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.Cat;

import java.util.List;
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

    /**
     * Отдает список айди юзеров, которые усыновили кошек.
     *
     * @return List, содержащий user_id из таблицы кошек. Если таких нет, то пустой List.
     */
    @Query(value = "SELECT cats.user_id FROM cats WHERE cats.user_id IS NOT NULL", nativeQuery = true)
    List<Long> getCatAdopters();

    void deleteByUserId(Long userId);

}
