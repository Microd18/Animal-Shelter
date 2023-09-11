package pro.sky.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pro.sky.AnimalShelter.enums.BotCommand;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "chat_state")
public class ChatState extends BaseEntity {

    @Column(name = "chat_id")
    private Long chatId;

    @Column
    @Enumerated(EnumType.STRING)
    private BotCommand twoStepBackState;

    @Column
    @Enumerated(EnumType.STRING)
    private BotCommand stepBackState;

    @Column
    @Enumerated(EnumType.STRING)
    private BotCommand currentState;

    @OneToOne(mappedBy = "chatState")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_bot_started")
    private boolean isBotStarted;
}
