package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.ChatState;

import java.util.Optional;

/**
 * Репозиторий для доступа к данным о состояниях чата.
 */
@Repository
public interface ChatStateRepository extends JpaRepository<ChatState, Long> {

    /**
     * Находит состояние чата по идентификатору чата.
     *
     * @param chatId Идентификатор чата.
     * @return Optional, содержащий состояние чата, если найдено, иначе пустой Optional.
     */
    Optional<ChatState> findByChatId(Long chatId);
}
