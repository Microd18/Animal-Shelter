package pro.sky.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "dogs")
@NoArgsConstructor
@AllArgsConstructor
public class Dog extends BaseEntity {

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "age")
    private Integer age;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "dog", cascade = CascadeType.ALL)
    private DogReport dogReport;

    @Override
    public String toString() {
        String userName = Objects.isNull(user) ? null : user.getUsername();
        return "Собака: id=" + super.getId() +
                ", nickname=" + nickname +
                ", age=" + age +
                ", user=" + userName;
    }

}
