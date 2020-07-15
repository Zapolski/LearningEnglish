package by.zapolski.english.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Сущность для языка, на который переведны английские фразы.
 */
@Entity
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "value")
public class Language implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Поддерживаемй язык, на котороый присутствует перевод
     */
    @Column(
            nullable = false,
            unique = true
    )
    private String value;

    public Language(String value) {
        this.value = value;
    }
}
