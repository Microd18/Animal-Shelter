package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.DogPhoto;

/**
 * Репозиторий для доступа к данным о фотографиях собак.
 */
@Repository
public interface DogPhotoRepository extends JpaRepository<DogPhoto, Long> {
}
