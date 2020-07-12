package by.zapolski.english.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Сущность для контекста-определения изучаемого слова в фразе-примере
 * Английские слова имеют множество значений
 * Контекст описывает определение изучаемого слова для конкретной фразы-примера
 */
@Entity
@Table
@Data
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
public class Context implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Текстовое описание контекста-определения изучаемого слова на английском языке
     */
    @Column(nullable = false)
    private String value;

}
