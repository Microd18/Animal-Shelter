package pro.sky.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Сущность, представляющая пользователя.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    /**
     * Имя пользователя.
     */
    @Column(name = "username")
    private String username;

    /**
     * Номер телефона пользователя.
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Адрес электронной почты пользователя.
     */
    @Column(name = "email")
    private String email;

    /**
     * Чат, связанный с пользователем.
     */
    @OneToOne()
    @JoinColumn(name = "chat_id")
    private Chat chat;
}
