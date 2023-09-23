package pro.sky.AnimalShelter.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

/**
 * Сущность, представляющая состояние чата.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "chat_states")
public class ChatState extends BaseEntity {

    /**
     * Все состояния для каждого чата.
     */
    @Type(type = "json")
    @Column(name = "state_data", columnDefinition = "jsonb")
    private String stateData;

    /**
     * Чат, связанный с этим состоянием.
     */
    @OneToOne()
    @JoinColumn(name = "chat_id")
    private Chat chat;
}