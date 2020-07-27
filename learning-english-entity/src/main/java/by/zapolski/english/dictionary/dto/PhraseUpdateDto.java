package by.zapolski.english.dictionary.dto;

import lombok.Data;

@Data
public class PhraseUpdateDto {
    private Long id;
    private String translation;
    private String english;
    private String context;
    private Integer rank;
    private String language;
}
