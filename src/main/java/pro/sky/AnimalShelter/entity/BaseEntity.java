package pro.sky.AnimalShelter.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Базовый класс для сущностей, содержащих идентификатор.
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    /**
     * Уникальный идентификатор сущности.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

}
