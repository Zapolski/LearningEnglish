package by.zapolski.english.service.learning.api;

import by.zapolski.english.learning.domain.Phrase;
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
     * Каждому слову сопоставлен набор фраз-примеров
     * Метод возвращает список фраз для конкретного слова
     *
     * @param word английское слово
     * @param minRank минимальный ранг для искомых фраз
     * @param maxRank максимальный ранг для искомых фраз
     * @param language язык перевода
     * @return список фраз-примеров связанных с изучаемым словом
     */
    List<Phrase> getPhrasesByWord(String word, Integer minRank, Integer maxRank, String language);

    /**
     * Каждой фразе присвоен ранг, он соответствует рагу самоого редкоиспользуемого слова
     * Метод возвращает фразы в дипазаон рангов
     *
     * @param minRank минимальный ранг для искомы фраз
     * @param maxRank максимальный ранг для искомых фраз
     * @param language язык перевода
     * @return список фраз-примеров воходящик в диапозон рангов
     */
    List<Phrase> getAllPhrasesWithRank(Integer minRank, Integer maxRank, String language);

    /**
     * Обновляет фразу по данным, пришедшим с UI
     *
     * @param phraseUpdateDto обновленные поля для фразы-примера
     * @return DTO для обновленной фразы
     */
    Phrase updatePhrase(PhraseUpdateDto phraseUpdateDto);
}
