package by.zapolski.english.learning.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Сущность для хранения перевода английской фразы на определенный язык
 */
@Entity
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "value")
public class Translation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST,CascadeType.REFRESH} // при создании переревода для нового языка, язык должен попасть в БД
    )
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne (
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "phrase_id")
    @ToString.Exclude
    private Phrase phrase;

    @Column(
            nullable = false,
            length = 500
    )
    private String value;

    public Translation(String value) {
        this.value = value;
    }
}
