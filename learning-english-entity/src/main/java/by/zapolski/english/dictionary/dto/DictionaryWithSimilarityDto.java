package by.zapolski.english.dictionary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryWithSimilarityDto {
    private Long id;
    private String value;
    private Integer rank;
    private Character partOfSpeech;
    private Double similarity;
}
