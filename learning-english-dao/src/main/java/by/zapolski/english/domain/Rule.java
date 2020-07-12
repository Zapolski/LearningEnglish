package by.zapolski.english.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность для правила английского языка
 *
 * Каждый пример-фраза может "подчиняться" различным грамматическим правилам
 * Изучая грамматику можно выбрать примеры для конкретного правила-раздела грамматики
 */
@Entity
@Table
@Data
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
public class Rule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "rules")
    private Set<Phrase> phrases = new HashSet<>();

    /**
     * Текстовое описание грамматического правила
     */
    @Column(nullable = false)
    private String value;

}
