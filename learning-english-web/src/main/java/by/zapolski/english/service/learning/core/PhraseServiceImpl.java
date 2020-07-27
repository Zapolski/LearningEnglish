package by.zapolski.english.service.learning.core;

import by.zapolski.english.dictionary.dto.PhraseUpdateDto;
import by.zapolski.english.learning.domain.Context;
import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.domain.Translation;
import by.zapolski.english.learning.dto.PhraseDto;
import by.zapolski.english.learning.mapper.PhraseMapper;
import by.zapolski.english.repository.learning.ContextRepository;
import by.zapolski.english.repository.learning.PhraseRepository;
import by.zapolski.english.repository.learning.TranslationRepository;
import by.zapolski.english.service.learning.api.PhraseService;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Log4j2
public class PhraseServiceImpl implements PhraseService {

    @Autowired
    PhraseRepository phraseRepository;

    @Autowired
    ContextRepository contextRepository;

    @Autowired
    PhraseMapper phraseMapper = Mappers.getMapper(PhraseMapper.class);

    @Override
    public List<PhraseDto> getPhrasesByWord(String word, Integer minRank, Integer maxRank, String language) {
        List<Phrase> phrases = phraseRepository.getPhrasesForWord(word, minRank, maxRank, Sort.by("id"));

        List<PhraseDto> result = phrases.stream()
                .map(phrase -> phraseMapper.phraseToDto(phrase))
                .collect(Collectors.toList());
        result = filterByLanguage(result, language);

        return result;
    }

    @Override
    public List<PhraseDto> getAllPhrasesWithRank(Integer minRank, Integer maxRank, String language) {
        List<Phrase> phrases = phraseRepository.getPhrasesByRanks(minRank, maxRank, Sort.by("id"));

        List<PhraseDto> result = phrases.stream()
                .map(phrase -> phraseMapper.phraseToDto(phrase))
                .collect(Collectors.toList());
        result = filterByLanguage(result, language);

        return result;
    }

    @Override
    @Transactional
    public PhraseDto updatePhrase(PhraseUpdateDto phraseUpdateDto) {

        Phrase phrase = phraseRepository.getOne(phraseUpdateDto.getId());
        phrase.setRank(phraseUpdateDto.getRank());
        phrase.setValue(phraseUpdateDto.getEnglish());

        Context context = contextRepository.getByValue(phraseUpdateDto.getContext());
        if (context == null) {
            context = new Context();
            context.setValue(phraseUpdateDto.getContext());
        }
        phrase.setContext(context);

        Translation translation = phrase.getTranslations()
                .stream()
                .filter(t -> t.getLanguage().getValue().equals(phraseUpdateDto.getLanguage()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't update not present translation."));

        translation.setValue(phraseUpdateDto.getTranslation());

        phraseRepository.save(phrase);

        PhraseDto phraseDto = phraseMapper.phraseToDto(phrase);
        log.info(phraseDto);

        return null;
    }

    private List<PhraseDto> filterByLanguage(List<PhraseDto> list, String language) {
        return list.stream().peek(
                ph -> ph.setTranslations(
                        ph.getTranslations().stream()
                                .filter(t -> t.getLanguage().getValue().equals(language))
                                .collect(Collectors.toList())
                )
        ).collect(Collectors.toList());
    }

}
