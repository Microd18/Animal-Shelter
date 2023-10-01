package pro.sky.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.AnimalShelter.entity.User;

import java.util.List;
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

    @Query(value = "SELECT u.* " +
            "FROM users u " +
            "LEFT JOIN cat_reports cr ON u.id = cr.user_id " +
            "LEFT JOIN dog_reports dr ON u.id = dr.user_id " +
            "WHERE (cr.created + INTERVAL '2 days' < NOW() OR dr.created + INTERVAL '2 days' < NOW())",
            nativeQuery = true)
    List<User> findUsersWithOldReports();
}
