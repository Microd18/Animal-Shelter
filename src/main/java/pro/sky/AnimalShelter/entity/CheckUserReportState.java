package pro.sky.AnimalShelter.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import pro.sky.AnimalShelter.enums.CheckUserReportStates;

import javax.persistence.*;
import java.util.Deque;

/**
 * Сущность, представляющая состояние проверки и оценки отчета усыновителя.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "check_user_reports_states")
public class CheckUserReportState extends BaseEntity {

    /**
     * Все состояния для каждого чата.
     */
    @Type(type = "json")
    @Column(name = "state_data", columnDefinition = "jsonb")
    private Deque<CheckUserReportStates> stateData;

    /**
     * Чат, связанный с этим состоянием.
     */
    @OneToOne()
    @JoinColumn(name = "chat_id")
    private Chat chat;
}
