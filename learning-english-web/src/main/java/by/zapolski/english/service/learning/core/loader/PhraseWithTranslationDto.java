package by.zapolski.english.service.learning.core.loader;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PhraseWithTranslationDto {
    private String wordValue;
    private Integer wordRank;

    private String phraseValue;
    private Integer phraseRank;

    private String resourcePath;
    private String resourceStorageType;
    private Long resourceSize;
    private String resourceChecksum;
    private Long resourceDuration;

    private String contextValue;

    private String translationValue;
    private String translationLanguageValue;

    private List<String> rules = new ArrayList<>();
}
