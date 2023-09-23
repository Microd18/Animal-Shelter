package pro.sky.AnimalShelter.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "photos")
@NoArgsConstructor
@AllArgsConstructor
public class Photo extends BaseEntity {

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "media_type")
    private String mediaType;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @OneToOne(mappedBy = "photo", cascade = CascadeType.ALL)
    private Report report;
}
