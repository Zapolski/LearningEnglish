package by.zapolski.english.service.lemma.core;

import by.zapolski.english.lemma.domain.Lemma;
import by.zapolski.english.lemma.dto.LemmaWithSimilarityDto;
import by.zapolski.english.repository.dictionary.LemmaRepository;
import by.zapolski.english.service.CrudBaseServiceImpl;
import by.zapolski.english.service.lemma.api.LemmaService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class LemmaServiceImpl extends CrudBaseServiceImpl<Lemma, Long> implements LemmaService {

    private final LemmaRepository lemmaRepository;

    public LemmaServiceImpl(LemmaRepository lemmaRepository) {
        this.lemmaRepository = lemmaRepository;
    }

    @Override
    public List<LemmaWithSimilarityDto> getSimilarWordsWithAccuracyThreshold(String word, Integer threshold) {
        List<LemmaWithSimilarityDto> result = new ArrayList<>();
        JaroWinklerSimilarity jaroWinklerSimilarity = new JaroWinklerSimilarity();

        List<Lemma> wordList = getAll();

        for (Lemma lemma : wordList) {
            double currentThreshold = jaroWinklerSimilarity.apply(word, lemma.getValue()) * 100;
            currentThreshold = Math.round(currentThreshold * 10d) / 10d;
            if (currentThreshold >= threshold) {
                LemmaWithSimilarityDto lemmaWithSimilarityDto = new LemmaWithSimilarityDto();
                lemmaWithSimilarityDto.setSimilarity(currentThreshold);
                lemmaWithSimilarityDto.setId(lemma.getId());
                lemmaWithSimilarityDto.setPartOfSpeech(lemma.getPartOfSpeech());
                lemmaWithSimilarityDto.setRank(lemma.getRank());
                lemmaWithSimilarityDto.setValue(lemma.getValue());
                result.add(lemmaWithSimilarityDto);
            }
        }

        return result.stream().sorted(
                Comparator.comparingDouble(LemmaWithSimilarityDto::getSimilarity)
                        .reversed()
                        .thenComparing(LemmaWithSimilarityDto::getRank)
        ).collect(Collectors.toList());
    }

    @Override
    public List<Lemma> getByRank(Integer rank) {
        return lemmaRepository.getByRank(rank);
    }

}
