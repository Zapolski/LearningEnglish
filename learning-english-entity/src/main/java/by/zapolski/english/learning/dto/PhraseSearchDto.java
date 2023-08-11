package by.zapolski.english.learning.dto;

import by.zapolski.english.learning.domain.enums.LearningStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Критерии поиска фраз
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhraseSearchDto {

    /**
     * Минимальный ранг фразы.
     */
    Integer minRank;

    /**
     * Максимальный ранг фразы.
     */
    Integer maxRank;

    /**
     * Язык перевода.
     */
    String language;

    /**
     * Базовове слова для примеров.
     */
    String word;

    /**
     * Список рангов
     */
    List<Integer> ranks;

    /**
     * Текстовый запрос (для SQL like).
     */
    String textQuery;

    /**
     * Статус изучения фразы.
     */
    LearningStatus learningStatus;
}
