package by.zapolski.english.learning.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Сущность для английского слова-леммы - глагол в необпределенной форме, существительное - в ед.числе,
 * фразовый глагол (verb + " " + preposition)
 */
@Entity
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "value")
@BatchSize(size = 10)
public class Word implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Слово
     */
    @Column(
            nullable = false,
            unique = true
    )
    private String value;

    /**
     * Ранг слова по частоте использования
     * чем меньше значение ранга, тем чаще в общем используется слово
     */
    @Column(nullable = false)
    private Integer rank;

    public Word(String value, Integer rank) {
        this.value = value;
        this.rank = rank;
    }
}
