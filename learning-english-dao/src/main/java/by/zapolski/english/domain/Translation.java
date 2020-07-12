package by.zapolski.english.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Сущность для хранения перевода английской фразы на определенный язык
 */
@Entity
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "phrase_id")
    private Phrase phrase;

    @Column(nullable = false)
    private String value;

}
