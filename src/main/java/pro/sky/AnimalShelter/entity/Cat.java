package pro.sky.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Класс представляет собой сущность "Кот" в базе данных.
 * Коты хранятся в таблице "cats".
 */
@Entity
@Getter
@Setter
@Table(name = "cats")
@NoArgsConstructor
@AllArgsConstructor
public class Cat extends BaseEntity {

    /**
     * Поле для хранения клички кота.
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * Поле для хранения возраста кота.
     */
    @Column(name = "age")
    private Integer age;

    /**
     * Поле для хранения связи кота с пользователем.
     * Один пользователь может иметь только одного кота.
     */
    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Поле для обратной связи с отчетом о коте.
     * Один кот может иметь только один отчет.
     */
    @OneToOne(mappedBy = "cat")
    private CatReport catReport;
}
