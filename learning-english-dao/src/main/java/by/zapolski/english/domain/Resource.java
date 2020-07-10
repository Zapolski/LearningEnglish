package by.zapolski.english.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Сущность для хранания информации о файле-треке с произношением английской фразы
 */
@Entity
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип хранилищиа {@link Storage}
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Storage storage;

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
    private Integer duration;
}
