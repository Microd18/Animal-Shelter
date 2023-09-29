package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.AnimalShelter.entity.VolunteerInfoCat;

import java.util.Optional;

public interface VolunteerInfoCatRepository extends JpaRepository<VolunteerInfoCat, Long> {
    Optional<VolunteerInfoCat> findByUserId(Long chatId);
}
