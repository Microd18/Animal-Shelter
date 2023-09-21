package pro.sky.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pro.sky.AnimalShelter.enums.BotCommand;

import javax.persistence.*;

/**
 * Сущность, представляющая состояние чата.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "chat_state")
public class ChatState extends BaseEntity {

    /**
     * Состояние два шага назад.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private BotCommand twoStepBackState;

    /**
     * Состояние шага назад.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private BotCommand stepBackState;

    /**
     * Текущее состояние.
     */
    @Column
    @Enumerated(EnumType.STRING)
    private BotCommand currentState;

    /**
     * Чат, связанный с этим состоянием.
     */
    @OneToOne()
    @JoinColumn(name = "chat_id")
    private Chat chat;
}
