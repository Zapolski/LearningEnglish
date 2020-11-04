package by.zapolski.english.learning.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность для хранения английской фразы
 */
@Entity
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "value")
public class Phrase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Английское слово связанное с примером-фразой
     */
    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, // при создании фразы-примера с новым словом, слово должно попасть в БД
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "word_id")
    private Word word;

    /**
     * Ресурс для файла-трэка с произношением английской фразы
     */
    @OneToOne(
            cascade = CascadeType.ALL, // ресурс полностью привязана к фразе-примеру
            fetch = FetchType.LAZY
    )
    private Resource resource;

    /**
     * Контекст-определение изучаемого слова в данной фразе-примере
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH} // для нового контекста создается новая сущность
    )
    @JoinColumn(name = "context_id")
    private Context context;

    /**
     * Список доступных переводов для фразы-примера
     */
    @OneToMany(
            mappedBy = "phrase",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL // переводы полностью привязаны к фразе-примеру
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<Translation> translations = new ArrayList<>();

    /**
     * Список грамматических правил, которым подчинается фраза-пример
     */
    @ManyToMany(
            cascade = CascadeType.PERSIST, // для нового правила создается новая сущность
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "phrase_rule",
            joinColumns = @JoinColumn(name = "phrase_id"),
            inverseJoinColumns = @JoinColumn(name = "rule_id")
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<Rule> rules = new ArrayList<>();

    /**
     * Текстовое представление английской фразы-примера
     */
    @Column(
            nullable = false,
            length = 500
    )
    private String value;

    /**
     * Ранг фразы, определенный по наименее часто употребляемому слову во фразе
     * Позволяет фильтровать фразы по "сложности"
     */
    @Column(nullable = false)
    private Integer rank;

    public Phrase(String value, Integer rank) {
        this.value = value;
        this.rank = rank;
    }
}
