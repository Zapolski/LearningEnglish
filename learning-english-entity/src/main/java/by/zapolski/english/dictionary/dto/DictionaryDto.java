package by.zapolski.english.dictionary.dto;

import lombok.Data;

@Data
public class DictionaryDto {
    private Long id;
    private String value;
    private Integer rank;
    private Character partOfSpeech;
}
