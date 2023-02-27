package by.zapolski.english.service.learning.api;

import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.dto.PagePhraseDto;
import by.zapolski.english.learning.dto.PhraseSearchDto;
import by.zapolski.english.lemma.dto.PhraseUpdateDto;
import by.zapolski.english.learning.dto.PhraseDto;
import by.zapolski.english.service.CrudBaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с фразами
 */
@Service
public interface PhraseService extends CrudBaseService<Phrase, Long> {

    /**
     * Обновляет фразу по данным, пришедшим с UI
     *
     * @param phraseUpdateDto обновленные поля для фразы-примера
     * @return DTO для обновленной фразы
     */
    Phrase updatePhrase(PhraseUpdateDto phraseUpdateDto);

    /**
     * Поиск фраз по критериям.
     *
     * @param searchDto критерии поиска
     * @return страница с фразами
     */
    PagePhraseDto search(PhraseSearchDto searchDto);

    /**
     * Поиск фраз по регулярному выражению POSIX PostgreSQL
     *
     * @param query шаблон POSIX
     * @param minRank минимальный ранг
     * @param maxRank максимальный ранг
     * @return страница с фразами
     */
    PagePhraseDto getByPattern(String query, Integer minRank, Integer maxRank);

    /**
     * Возвращает случайное количество фраз
     *
     * @param count необходимое количество фраз
     * @return страница с фразами
     */
    PagePhraseDto getRandomCount(Long count);
}
