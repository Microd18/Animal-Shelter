package pro.sky.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Класс представляет собой сущность "Список отчетов по собакам для волонтера" в базе данных.
 */
@Entity
@Getter
@Setter
@Table(name = "volunteer_info_dog")
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerInfoDog extends BaseEntity {

    /**
     * Поле для хранения количества дней содержания собаки у усыновителя.
     */
    @Column(name = "amount_of_days")
    private Integer amountOfDays;

    /**
     * Поле для хранения оценки отчета.
     */
    @Column(name = "rating")
    private Double rating;

    /**
     * Поле для хранения связи отчета с пользователем.
     * Один пользователь может иметь только один отчет.
     */
    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Поле для хранения значения отображающего дополнительный (14 дневный) срок для усыновления.
     */
    @Column(name = "extra_days")
    private int extraDays;

}
