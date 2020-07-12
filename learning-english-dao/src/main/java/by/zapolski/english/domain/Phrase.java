package by.zapolski.english.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность для хранения английской фразы
 */
@Entity
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Английское слово связанное с примером-фразой
     */
    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    /**
     * Ресурс для файла-трэка с произношением английской фразы
     */
    @OneToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    /**
     * Контекст-определение изучаемого слова в данной фразе-примере
     */
    @ManyToOne
    @JoinColumn(name = "context_id")
    private Context context;

    /**
     * Список доступных переводов для фразы-примера
     */
    @OneToMany(mappedBy = "phrase")
    private Set<Translation> translations;

    /**
     * Список грамматических правил, которым подчинается фраза-пример
     */
    @ManyToMany
    @JoinTable(
            name = "phrase_rule",
            joinColumns = @JoinColumn(name = "phrase_id"),
            inverseJoinColumns = @JoinColumn(name = "rule_id"))
    private Set<Rule> rules = new HashSet<>();

    /**
     * Текстовое представление английской фразы-примера
     */
    @Column(nullable = false)
    private String value;

    /**
     * Ранг фразы, определенный по наименее часто употребляемому слову во фразе
     * Позволяет фильтровать фразы по "сложности"
     */
    @Column(nullable = false)
    private Integer rank;
}
