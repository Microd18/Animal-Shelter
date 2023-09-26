package pro.sky.AnimalShelter.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс представляет собой сущность "Отчет о коте" в базе данных.
 * Отчеты о котах хранятся в таблице "cat_reports".
 */
@Entity
@Getter
@Setter
@Builder
@Table(name = "cat_reports")
@NoArgsConstructor
@AllArgsConstructor
public class CatReport extends BaseEntity {

    /**
     * Поле для обратной связи с фотографией кота, связанной с отчетом.
     * Каждый отчет о коте может содержать одну фотографию кота.
     */
    @OneToOne()
    @JoinColumn(name = "photo_id")
    private CatPhoto catPhoto;

    /**
     * Поле для хранения информации о рационе кота, в виде текстовых данных.
     */
    @Lob
    @Column(name = "ration")
    private String ration;

    /**
     * Поле для хранения информации о состоянии здоровья кота, в виде текстовых данных.
     */
    @Lob
    @Column(name = "well_being")
    private String wellBeing;

    /**
     * Поле для хранения информации о поведении кота, в виде текстовых данных.
     */
    @Lob
    @Column(name = "behavior")
    private String behavior;

    /**
     * Поле для хранения времени последнего обновления отчета о коте.
     */
    @Column(name = "updated")
    private LocalDateTime updated;

    /**
     * Поле для хранения времени создания отчета о коте.
     */
    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime created;

    /**
     * Поле для обратной связи с пользователем, создавшим отчет о коте.
     * Один пользователь может создать несколько отчетов о котах.
     */
    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Поле для обратной связи с котом, к которому относится отчет.
     * Один отчет о коте относится к одному коту.
     */
    @OneToOne()
    @JoinColumn(name = "cat_id")
    private Cat cat;
}
