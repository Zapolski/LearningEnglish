package by.zapolski.english.service.dictionary.api;

import by.zapolski.english.dictionary.domain.Dictionary;
import by.zapolski.english.dictionary.dto.DictionaryDto;
import by.zapolski.english.dictionary.dto.DictionaryWithSimilarityDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы со словарем
 */
@Service
public interface DictionaryService {

    /**
     * Опеределяет список слов похожих на искомо с определенной точностью
     *
     * @param word искомое солов
     * @param threshold точность схожести в процентах
     * @return список слов из словаря
     */
    List<DictionaryWithSimilarityDto> getSimilarWordsWithAccuracyThreshold(String word, Integer threshold);

    Dictionary save(DictionaryDto wordWithFrequency);

}