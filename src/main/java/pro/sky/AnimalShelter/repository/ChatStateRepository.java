package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.AnimalShelter.entity.ChatState;
import pro.sky.AnimalShelter.entity.User;

import java.util.Optional;

public interface ChatStateRepository extends JpaRepository<ChatState, Long> {
    Optional<ChatState> findByChatId(Long chatId);
}
