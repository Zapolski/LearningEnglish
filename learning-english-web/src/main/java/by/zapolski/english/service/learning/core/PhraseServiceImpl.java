package by.zapolski.english.service.learning.core;

import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.domain.Translation;
import by.zapolski.english.learning.dto.PhraseDto;
import by.zapolski.english.learning.mapper.PhraseMapper;
import by.zapolski.english.repository.learning.PhraseRepository;
import by.zapolski.english.repository.learning.TranslationRepository;
import by.zapolski.english.service.learning.api.PhraseService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PhraseServiceImpl implements PhraseService {

    @Autowired
    PhraseRepository phraseRepository;

    @Autowired
    PhraseMapper phraseMapper = Mappers.getMapper(PhraseMapper.class);

    @Override
    public List<PhraseDto> getPhrasesByWord(String word, Integer minRank, Integer maxRank, String language) {
        List<Phrase> phrases = phraseRepository.getPhrasesForWord(word, minRank, maxRank, Sort.by("id"));
        return phrases.stream()
                .map(phrase -> phraseMapper.phraseToDto(phrase))
                .collect(Collectors.toList());
    }

    @Override
    public List<PhraseDto> getAllPhrasesWithRank(Integer minRank, Integer maxRank, String language) {
        List<Phrase> phrases = phraseRepository.getPhrasesByRanks(minRank, maxRank, Sort.by("id"));
        return phrases.stream()
                .map(phrase -> phraseMapper.phraseToDto(phrase))
                .collect(Collectors.toList());
    }
}
