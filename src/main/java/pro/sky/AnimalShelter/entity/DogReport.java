package pro.sky.AnimalShelter.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс представляет сущность "Отчет о собаке" в базе данных.
 * Отчеты о собаках хранятся в таблице "dog_reports".
 */
@Entity
@Getter
@Setter
@Builder
@Table(name = "dog_reports")
@NoArgsConstructor
@AllArgsConstructor
public class DogReport extends BaseEntity {

    /**
     * Поле для хранения информации оценен отчет волонтером или нет.
     */
    @Column(name = "report_verified", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean reportVerified;

    /**
     * Поле для связи отчета о собаке с фотографией собаки.
     * Один отчет о собаке связан с одной фотографией собаки.
     */
    @OneToOne()
    @JoinColumn(name = "photo_id")
    private DogPhoto dogPhoto;

    /**
     * Поле для хранения рациона собаки в отчете.
     */
    @Lob
    @Column(name = "ration")
    private String ration;

    /**
     * Поле для хранения информации о благополучии собаки в отчете.
     */
    @Lob
    @Column(name = "well_being")
    private String wellBeing;

    /**
     * Поле для хранения информации о поведении собаки в отчете.
     */
    @Lob
    @Column(name = "behavior")
    private String behavior;

    /**
     * Поле для хранения даты и времени последнего обновления отчета.
     */
    @Column(name = "updated")
    private LocalDateTime updated;

    /**
     * Поле для хранения даты и времени создания отчета.
     * Значение этого поля генерируется автоматически при создании записи.
     */
    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime created;

    /**
     * Поле для связи отчета о собаке с пользователем, создавшим отчет.
     * Один отчет о собаке связан с одним пользователем.
     */
    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Поле для связи отчета о собаке с собакой, к которой он относится.
     * Один отчет о собаке связан с одной собакой.
     */
    @OneToOne()
    @JoinColumn(name = "dog_id")
    private Dog dog;
}
