package by.zapolski.english.learning.dto;

import lombok.Data;

@Data
public class TranslationDto {

    private Long id;
    private LanguageDto language;
    private PhraseDto phrase;
    private String value;
}
