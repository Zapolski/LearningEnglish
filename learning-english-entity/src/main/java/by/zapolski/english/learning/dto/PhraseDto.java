package by.zapolski.english.learning.dto;

import by.zapolski.english.learning.domain.enums.LearningStatus;
import lombok.Data;

import java.util.List;

@Data
public class PhraseDto {
    private Long id;
    private WordDto word;
    private ResourceDto resource;
    private ContextDto context;
    private List<TranslationDto> translations;
    private List<RuleDto> rules;
    private String value;
    private Integer rank;
    private boolean isModified;
    private Integer successViewsCount;
    private LearningStatus learningStatus;
}
