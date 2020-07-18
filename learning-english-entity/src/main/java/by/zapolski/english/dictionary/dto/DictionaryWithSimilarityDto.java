package by.zapolski.english.dictionary.dto;

import lombok.Data;

@Data
public class DictionaryWithSimilarityDto {
    private Long id;
    private String value;
    private Integer rank;
    private Character partOfSpeech;
    private Double similarity;
}
