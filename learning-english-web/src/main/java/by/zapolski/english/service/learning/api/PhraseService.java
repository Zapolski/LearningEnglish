package by.zapolski.english.service.learning.api;

import by.zapolski.english.learning.dto.PhraseDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с фразами
 */
@Service
public interface PhraseService {

    /**
     * Каждому слову сопоставлен набор фраз-примеров
     * Метод возвращает список фраз для конкретного слова
     *
     * @param word английское слово
     * @param minRank минимальный ранг для искомых фраз
     * @param maxRank максимальный ранг для искомых фраз
     * @return список фраз-примеров связанных с изучаемым словом
     */
    List<PhraseDto> getPhrasesByWord(String word, Integer minRank, Integer maxRank);
}
