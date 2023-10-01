package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.AnimalShelter.entity.VolunteerInfoDog;

import java.util.Optional;

public interface VolunteerInfoDogRepository extends JpaRepository<VolunteerInfoDog, Long> {

    Optional<VolunteerInfoDog> findByUserId(Long chatId);

    void deleteByUserId(Long userId);

}
