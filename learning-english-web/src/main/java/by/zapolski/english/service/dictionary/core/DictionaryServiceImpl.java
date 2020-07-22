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

        List<Dictionary> wordList = dictionaryRepository.findAll();

        for (Dictionary dictionary : wordList) {
            Double currentThreshold = jaroWinklerSimilarity.apply(word, dictionary.getValue()) * 100;

            currentThreshold = Math.round(currentThreshold * 10d) / 10d;

            if (currentThreshold >= threshold) {
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
    public DictionaryDto save(DictionaryDto dictionaryDto) {
        Dictionary dictionary = dictionaryMapper.dtoToDictionary(dictionaryDto);
        dictionaryRepository.save(dictionary);
        return dictionaryMapper.dictionaryToDto(dictionary);
    }

    @Override
    public void delete(DictionaryDto dictionaryDto) {
        Dictionary dictionary = dictionaryMapper.dtoToDictionary(dictionaryDto);
        dictionaryRepository.delete(dictionary);
    }

    @Override
    public void deleteById(Long id){
        dictionaryRepository.deleteById(id);
    }

    @Override
    public DictionaryDto getById(Long id) {
        Dictionary dictionary = dictionaryRepository.getOne(id);
        return dictionaryMapper.dictionaryToDto(dictionary);
    }

    @Override
    public List<DictionaryDto> getByRank(Integer rank) {
        List<Dictionary> result = dictionaryRepository.getByRank(rank);
        return result.stream()
                .map(dictionary -> dictionaryMapper.dictionaryToDto(dictionary))
                .collect(Collectors.toList());
    }

}
