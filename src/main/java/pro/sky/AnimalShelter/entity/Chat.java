package pro.sky.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Сущность, представляющая чат.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat extends BaseEntity {

    /**
     * Уникальный идентификатор чата.
     */
    @Column(name = "chat_id")
    private Long chatId;

    /**
     * Флаг, указывающий, запущен ли бот в этом чате.
     */
    @Column(name = "bot_started")
    private boolean botStarted;

    /**
     * Пользователь, связанный с этим чатом.
     */
    @OneToOne(mappedBy = "chat", cascade = CascadeType.ALL)
    private User user;

    /**
     * Состояние чата.
     */
    @OneToOne(mappedBy = "chat", cascade = CascadeType.ALL)
    private ChatState chatState;
}
