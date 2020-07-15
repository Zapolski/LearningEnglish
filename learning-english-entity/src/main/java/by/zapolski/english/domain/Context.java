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
@EqualsAndHashCode(of = "value")
@NoArgsConstructor
public class Context implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Текстовое описание контекста-определения изучаемого слова на английском языке
     */
    @Column(
            nullable = false,
            unique = true
    )
    private String value;

    public Context(String value) {
        this.value = value;
    }
}
