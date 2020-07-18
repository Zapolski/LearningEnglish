package by.zapolski.english.service.dictionary.core;

import by.zapolski.english.dictionary.domain.Dictionary;
import by.zapolski.english.dictionary.dto.DictionaryDto;
import by.zapolski.english.dictionary.dto.DictionaryWithSimilarityDto;
import by.zapolski.english.dictionary.mapper.DictionaryMapper;
import by.zapolski.english.repository.dictionary.DictionaryRepository;
import by.zapolski.english.service.dictionary.api.DictionaryService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DictionaryServiceImpl implements DictionaryService {

    private DictionaryMapper dictionaryMapper = Mappers.getMapper(DictionaryMapper.class);

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Override
    public List<DictionaryWithSimilarityDto> getSimilarWordsWithAccuracyThreshold(String word, Integer threshold) {
        List<DictionaryWithSimilarityDto> result = new ArrayList<>();
        JaroWinklerSimilarity jaroWinklerSimilarity = new JaroWinklerSimilarity();

        log.info("Loading dictionary to memory...");
        List<Dictionary> wordList = dictionaryRepository.findAll();

        log.info("Looking for similar words...");
        for (Dictionary dictionary : wordList) {
            //log.info("Calculate similarity [{}] with [{}]",word, dictionary.getValue());
            Double currentThreshold = jaroWinklerSimilarity.apply(word, dictionary.getValue()) * 100;

            if (currentThreshold >= threshold) {
                log.info("----> Similarity fits. Create object for response.");
                DictionaryWithSimilarityDto dictionaryWithSimilarityDto = new DictionaryWithSimilarityDto();

                dictionaryWithSimilarityDto.setSimilarity(currentThreshold);
                dictionaryWithSimilarityDto.setId(dictionary.getId());
                dictionaryWithSimilarityDto.setPartOfSpeech(dictionary.getPartOfSpeech());
                dictionaryWithSimilarityDto.setRank(dictionary.getRank());
                dictionaryWithSimilarityDto.setValue(dictionary.getValue());

                result.add(dictionaryWithSimilarityDto);
            }
        }
        return result.stream().sorted(
                Comparator.comparingDouble(DictionaryWithSimilarityDto::getSimilarity)
                        .reversed()
                        .thenComparing(DictionaryWithSimilarityDto::getRank)
        ).collect(Collectors.toList());
    }

    @Override
    public Dictionary save(DictionaryDto dictionaryDto) {
        Dictionary dictionary = dictionaryMapper.dtoToDictionary(dictionaryDto);
        return dictionaryRepository.save(dictionary);
    }
}
