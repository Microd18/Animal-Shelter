package pro.sky.AnimalShelter.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Класс представляет собой сущность "Фотография кота" в базе данных.
 * Фотографии котов хранятся в таблице "cat_photos".
 */
@Getter
@Setter
@Entity
@Builder
@Table(name = "cat_photos")
@NoArgsConstructor
@AllArgsConstructor
public class CatPhoto extends BaseEntity {

    /**
     * Поле для хранения размера файла фотографии кота.
     */
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * Поле для хранения типа медиафайла фотографии кота.
     * Например, image/jpeg.
     */
    @Column(name = "media_type")
    private String mediaType;

    /**
     * Поле для хранения бинарных данных фотографии кота.
     * Фактически, это содержимое файла фотографии.
     */
    @Lob
    @Column(name = "data")
    private byte[] data;

    /**
     * Поле для обратной связи с отчетом о коте, к которому принадлежит фотография.
     * Одна фотография кота связана с одним отчетом о коте.
     */
    @OneToOne(mappedBy = "catPhoto")
    private CatReport catReport;
}
