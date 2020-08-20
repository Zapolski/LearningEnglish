package by.zapolski.english.service.lemma.api;

import by.zapolski.english.lemma.domain.Lemma;
import by.zapolski.english.lemma.dto.LemmaWithSimilarityDto;
import by.zapolski.english.service.CrudBaseService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы со словарем
 */
@Service
public interface LemmaService extends CrudBaseService<Lemma, Long> {

    /**
     * Опеределяет список слов похожих на искомое с определенным процентом схожести
     *
     * @param word      искомое солов
     * @param threshold точность схожести в процентах
     * @return список слов из словаря
     */
    List<LemmaWithSimilarityDto> getSimilarWordsWithAccuracyThreshold(String word, Integer threshold);

    /**
     * Получение списка лемм по рангу
     *
     * @param rank ранг
     * @return список лемм по рангку
     */
    List<Lemma> getByRank(Integer rank);
}
