package by.zapolski.english.service.lemma.api;

import by.zapolski.english.lemma.dto.LemmaDto;
import by.zapolski.english.lemma.dto.LemmaWithSimilarityDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы со словарем
 */
@Service
public interface LemmaService {

    /**
     * Опеределяет список слов похожих на искомо с определенной точностью
     *
     * @param word искомое солов
     * @param threshold точность схожести в процентах
     * @return список слов из словаря
     */
    List<LemmaWithSimilarityDto> getSimilarWordsWithAccuracyThreshold(String word, Integer threshold);

    LemmaDto save(LemmaDto wordWithFrequency);

    void delete(LemmaDto lemmaDto);

    void deleteById(Long id);

    LemmaDto getById(Long id);

    List<LemmaDto> getByRank(Integer rank);
}
