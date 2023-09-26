package pro.sky.AnimalShelter.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Класс представляет сущность "Фотография собаки" в базе данных.
 * Фотографии собак хранятся в таблице "dog_photos".
 */
@Getter
@Setter
@Entity
@Builder
@Table(name = "dog_photos")
@NoArgsConstructor
@AllArgsConstructor
public class DogPhoto extends BaseEntity {

    /**
     * Поле для хранения размера файла фотографии собаки.
     */
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * Поле для хранения типа медиафайла фотографии собаки.
     * Например, image/jpeg.
     */
    @Column(name = "media_type")
    private String mediaType;

    /**
     * Поле для хранения бинарных данных фотографии собаки.
     * Для больших данных используется тип LOB (Large Object).
     */
    @Lob
    @Column(name = "data")
    private byte[] data;

    /**
     * Поле для обратной связи с отчетом о собаке, связанным с данной фотографией.
     * Каждая фотография собаки может быть связана с одним отчетом о собаке.
     */
    @OneToOne(mappedBy = "dogPhoto")
    private DogReport dogReport;
}
