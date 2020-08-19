package by.zapolski.english.service.lemma.core;

import by.zapolski.english.lemma.domain.Lemma;
import by.zapolski.english.lemma.dto.LemmaDto;
import by.zapolski.english.lemma.dto.LemmaWithSimilarityDto;
import by.zapolski.english.lemma.mapper.LemmaMapper;
import by.zapolski.english.repository.dictionary.LemmaRepository;
import by.zapolski.english.service.lemma.api.LemmaService;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LemmaServiceImpl implements LemmaService {

    private final LemmaRepository lemmaRepository;

    public LemmaServiceImpl(LemmaRepository lemmaRepository) {
        this.lemmaRepository = lemmaRepository;
    }

    @Override
    public List<LemmaWithSimilarityDto> getSimilarWordsWithAccuracyThreshold(String word, Integer threshold) {
        List<LemmaWithSimilarityDto> result = new ArrayList<>();
        JaroWinklerSimilarity jaroWinklerSimilarity = new JaroWinklerSimilarity();

        List<Lemma> wordList = lemmaRepository.findAll();

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
    public Lemma save(Lemma lemma) {
        return lemmaRepository.save(lemma);
    }

    @Override
    public void delete(Lemma lemma) {
        lemmaRepository.delete(lemma);
    }

    @Override
    public void deleteById(Long id) {
        lemmaRepository.deleteById(id);
    }

    @Override
    public Lemma getById(Long id) {
        return lemmaRepository.getOne(id);
    }

    @Override
    public List<Lemma> getByRank(Integer rank) {
        return lemmaRepository.getByRank(rank);
    }

}
