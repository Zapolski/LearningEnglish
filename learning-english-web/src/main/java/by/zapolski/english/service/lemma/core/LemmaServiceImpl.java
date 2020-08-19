package by.zapolski.english.service.lemma.core;

import by.zapolski.english.lemma.domain.Lemma;
import by.zapolski.english.lemma.dto.LemmaDto;
import by.zapolski.english.lemma.dto.LemmaWithSimilarityDto;
import by.zapolski.english.lemma.mapper.LemmaMapper;
import by.zapolski.english.repository.dictionary.DictionaryRepository;
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

    private LemmaMapper lemmaMapper = Mappers.getMapper(LemmaMapper.class);

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public List<LemmaWithSimilarityDto> getSimilarWordsWithAccuracyThreshold(String word, Integer threshold) {
        List<LemmaWithSimilarityDto> result = new ArrayList<>();
        JaroWinklerSimilarity jaroWinklerSimilarity = new JaroWinklerSimilarity();

        List<Lemma> wordList = dictionaryRepository.findAll();

        for (Lemma lemma : wordList) {
            Double currentThreshold = jaroWinklerSimilarity.apply(word, lemma.getValue()) * 100;

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
    public LemmaDto save(LemmaDto lemmaDto) {
        Lemma lemma = lemmaMapper.dtoToLemma(lemmaDto);
        dictionaryRepository.save(lemma);
        return lemmaMapper.lemmaToDto(lemma);
    }

    @Override
    public void delete(LemmaDto lemmaDto) {
        Lemma lemma = lemmaMapper.dtoToLemma(lemmaDto);
        dictionaryRepository.delete(lemma);
    }

    @Override
    public void deleteById(Long id) {
        dictionaryRepository.deleteById(id);
    }

    @Override
    public LemmaDto getById(Long id) {
        Lemma lemma = dictionaryRepository.getOne(id);
        return lemmaMapper.lemmaToDto(lemma);
    }

    @Override
    public List<LemmaDto> getByRank(Integer rank) {
        List<Lemma> result = dictionaryRepository.getByRank(rank);
        return result.stream()
                .map(dictionary -> lemmaMapper.lemmaToDto(dictionary))
                .collect(Collectors.toList());
    }

}
