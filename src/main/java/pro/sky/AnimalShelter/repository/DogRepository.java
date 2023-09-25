package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.AnimalShelter.entity.Dog;

/**
 * Репозиторий для доступа к данным о собаках.
 */
public interface DogRepository extends JpaRepository<Dog, Long> {

}
