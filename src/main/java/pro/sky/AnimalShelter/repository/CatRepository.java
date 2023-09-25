package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.AnimalShelter.entity.Cat;

/**
 * Репозиторий для доступа к данным о кошках.
 */
public interface CatRepository extends JpaRepository<Cat, Long> {
}
