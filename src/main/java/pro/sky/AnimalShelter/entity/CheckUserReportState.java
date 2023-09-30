package pro.sky.AnimalShelter.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Builder;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Сущность, представляющая состояние проверки и оценки отчета усыновителя.
 */
@Entity
@Builder
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "check_user_reports_states")
public class CheckUserReportState extends ChatState {
}
