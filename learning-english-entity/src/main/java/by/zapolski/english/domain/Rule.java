package by.zapolski.english.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность для правила английского языка
 * <p>
 * Каждый пример-фраза может "подчиняться" различным грамматическим правилам
 * Изучая грамматику можно выбрать примеры для конкретного правила-раздела грамматики
 */
@Entity
@Table
@Data
@EqualsAndHashCode(of = "value")
@NoArgsConstructor
public class Rule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "rules", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Phrase> phrases = new ArrayList<>();

    /**
     * Текстовое описание грамматического правила
     */
    @Column(
            nullable = false,
            unique = true,
            length = 500
    )
    private String value;

    public Rule(String value) {
        this.value = value;
    }
}
