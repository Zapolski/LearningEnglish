package by.zapolski.english.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Сущность для английского слова (леммы) - глагол в необпределенной форме, существительное - в ед.числе,
 * фразовый глагол (verb + " " + preposition)
 */
@Entity
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class Word implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Слово
     */
    @Column(nullable = false)
    private String value;

    /**
     * Ранг слова по частоте использования
     * чем меньше значение ранга, тем чаще в общем используется слово
     */
    @Column(nullable = false)
    private Integer rank;
}
