package pro.sky.AnimalShelter.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

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
    private String stateData;

    /**
     * Чат, связанный с этим состоянием.
     */
    @OneToOne()
    @JoinColumn(name = "chat_id")
    private Chat chat;

}
