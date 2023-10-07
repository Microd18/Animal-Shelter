package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.Chat;

import java.util.Optional;

/**
 * Репозиторий для доступа к данным о чатах.
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    /**
     * Находит чат по его идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Optional, содержащий чат, если найден, иначе пустой Optional.
     */
    Optional<Chat> findByChatId(Long chatId);
}
