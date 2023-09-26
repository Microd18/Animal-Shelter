package pro.sky.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Класс представляет собой сущность "Собака" в базе данных.
 * Собаки хранятся в таблице "dogs".
 */
@Entity
@Getter
@Setter
@Table(name = "dogs")
@NoArgsConstructor
@AllArgsConstructor
public class Dog extends BaseEntity {

    /**
     * Поле для хранения клички собаки.
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * Поле для хранения возраста собаки.
     */
    @Column(name = "age")
    private Integer age;

    /**
     * Поле для обратной связи с пользователем, владельцем собаки.
     * Одна собака принадлежит одному пользователю.
     */
    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Поле для обратной связи с отчетом о собаке, связанным с данной собакой.
     * Каждая собака может иметь один отчет о собаке.
     */
    @OneToOne(mappedBy = "dog")
    private DogReport dogReport;
}
