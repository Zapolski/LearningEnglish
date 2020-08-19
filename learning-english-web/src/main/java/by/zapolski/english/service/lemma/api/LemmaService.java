package by.zapolski.english.service.lemma.api;

import by.zapolski.english.lemma.domain.Lemma;
import by.zapolski.english.lemma.dto.LemmaWithSimilarityDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы со словарем
 */
@Service
public interface LemmaService {

    /**
     * Опеределяет список слов похожих на искомое с определенным процентом схожести
     *
     * @param word      искомое солов
     * @param threshold точность схожести в процентах
     * @return список слов из словаря
     */
    List<LemmaWithSimilarityDto> getSimilarWordsWithAccuracyThreshold(String word, Integer threshold);

    Lemma save(Lemma wordWithFrequency);

    void delete(Lemma lemmaDto);

    void deleteById(Long id);

    Lemma getById(Long id);

    List<Lemma> getByRank(Integer rank);
}
