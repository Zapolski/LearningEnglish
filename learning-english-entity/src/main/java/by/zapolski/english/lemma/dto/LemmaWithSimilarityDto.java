package by.zapolski.english.lemma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LemmaWithSimilarityDto {
    private Long id;
    private String value;
    private Integer rank;
    private Character partOfSpeech;
    private Double similarity;
}
