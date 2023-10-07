package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.CatPhoto;

/**
 * Репозиторий для доступа к данным о фотографиях кошек.
 */
@Repository
public interface CatPhotoRepository extends JpaRepository<CatPhoto, Long> {
}
