package pro.sky.AnimalShelter.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import pro.sky.AnimalShelter.enums.UserReportStates;

import javax.persistence.*;
import java.util.Deque;

/**
 * Сущность, представляющая состояние принятия отчёта юзера.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "user_reports_states")
public class UserReportState extends BaseEntity {

    /**
     * Все состояния для каждого чата.
     */
    @Type(type = "json")
    @Column(name = "state_data", columnDefinition = "jsonb")
    private Deque<UserReportStates> stateData;

    /**
     * Чат, связанный с этим состоянием.
     */
    @OneToOne()
    @JoinColumn(name = "chat_id")
    private Chat chat;

}
