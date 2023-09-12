package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.AnimalShelter.entity.ChatState;
import pro.sky.AnimalShelter.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatStateId(Long chatStateId);
}
