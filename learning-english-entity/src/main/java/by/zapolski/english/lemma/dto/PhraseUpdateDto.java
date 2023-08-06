package by.zapolski.english.lemma.dto;

import by.zapolski.english.learning.domain.enums.LearningStatus;
import lombok.Data;

@Data
public class PhraseUpdateDto {
    private Long id;
    private String translation;
    private String english;
    private String context;
    private Integer rank;
    private String language;
    private LearningStatus learningStatus;
}
