package by.zapolski.english.learning.domain;

import by.zapolski.english.learning.domain.enums.StorageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Сущность для хранания информации о файле-треке с произношением английской фразы
 */
@Entity
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@BatchSize(size = 10)
public class Resource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип хранилищиа {@link StorageType}
     */
    @Column(nullable = false, name = "storage_name")
    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    /**
     * Путь
     * url - для облака
     * path - для локального хранения
     */
    @Column(nullable = false)
    private String path;

    /**
     * Размер в байтах
     */
    @Column(nullable = false)
    private Long size;

    /**
     * Контрольная сумма в байтах
     */
    @Column(nullable = false)
    private String checksum;

    /**
     * Длительность файла в милисекундах
     */
    @Column(nullable = false)
    private Long duration;

    public Resource(StorageType storageType, String path, Long size, String checksum, Long duration) {
        this.storageType = storageType;
        this.path = path;
        this.size = size;
        this.checksum = checksum;
        this.duration = duration;
    }
}
