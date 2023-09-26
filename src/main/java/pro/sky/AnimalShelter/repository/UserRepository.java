package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.User;

import java.util.Optional;

/**
 * Репозиторий для доступа к данным о юзере.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя чата по идентификатору чата.
     *
     * @param chatId Идентификатор чата.
     * @return Optional, содержащий состояние чата, если найдено, иначе пустой Optional.
     */
    Optional<User> findByChatId(Long chatId);
}
