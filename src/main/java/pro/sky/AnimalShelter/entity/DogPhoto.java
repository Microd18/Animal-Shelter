package pro.sky.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "dog_photos")
@NoArgsConstructor
@AllArgsConstructor
public class DogPhoto extends BaseEntity {

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "media_type")
    private String mediaType;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @OneToOne(mappedBy = "dogPhoto", cascade = CascadeType.ALL)
    private DogReport dogReport;
}
