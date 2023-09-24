package pro.sky.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "cat_reports")
@NoArgsConstructor
@AllArgsConstructor
public class CatReport extends BaseEntity {

    @OneToOne()
    @JoinColumn(name = "photo_id")
    private CatPhoto catPhoto;

    @Lob
    @Column(name = "ration")
    private String ration;

    @Lob
    @Column(name = "well_being")
    private String wellBeing;

    @Lob
    @Column(name = "behavior")
    private String behavior;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne()
    @JoinColumn(name = "cat_id")
    private Cat cat;
}
