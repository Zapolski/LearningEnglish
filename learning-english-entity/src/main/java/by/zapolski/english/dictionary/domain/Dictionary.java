package by.zapolski.english.dictionary.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table
@Entity
@Data
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
public class Dictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private Integer rank;

    @Column(nullable = false)
    private Character partOfSpeech;

    public Dictionary(String value, Integer rank, Character partOfSpeech) {
        this.value = value;
        this.rank = rank;
        this.partOfSpeech = partOfSpeech;
    }
}
