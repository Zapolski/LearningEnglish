package by.zapolski.english.learning.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

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
@EqualsAndHashCode(of = "value")
@NoArgsConstructor
@BatchSize(size = 10)
public class Context implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Текстовое описание контекста-определения изучаемого слова на английском языке
     */
    @Column(
            nullable = false,
            unique = true,
            length = 500
    )
    private String value;

    public Context(String value) {
        this.value = value;
    }
}
