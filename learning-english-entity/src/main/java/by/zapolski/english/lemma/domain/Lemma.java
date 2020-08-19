package by.zapolski.english.lemma.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table
@Entity
@Data
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
public class Lemma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Value is mandatory!")
    private String value;

    @Column(nullable = false)
    @NotNull(message = "Rank is mandatory!")
    private Integer rank;

    @Column(nullable = false)
    @NotNull(message = "Part of speech is mandatory!")
    private Character partOfSpeech;

    public Lemma(String value, Integer rank, Character partOfSpeech) {
        this.value = value;
        this.rank = rank;
        this.partOfSpeech = partOfSpeech;
    }
}
