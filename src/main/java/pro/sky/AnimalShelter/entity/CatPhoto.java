package pro.sky.AnimalShelter.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "cat_photos")
@NoArgsConstructor
@AllArgsConstructor
public class CatPhoto extends BaseEntity {

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "media_type")
    private String mediaType;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @OneToOne(mappedBy = "catPhoto", cascade = CascadeType.ALL)
    private CatReport catReport;
}
