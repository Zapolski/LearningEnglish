package by.zapolski.english.service.learning.core;

import by.zapolski.english.learning.domain.Context;
import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.domain.Translation;
import by.zapolski.english.lemma.dto.PhraseUpdateDto;
import by.zapolski.english.repository.learning.ContextRepository;
import by.zapolski.english.repository.learning.PhraseRepository;
import by.zapolski.english.service.CrudBaseServiceImpl;
import by.zapolski.english.service.learning.api.PhraseService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PhraseServiceImpl extends CrudBaseServiceImpl<Phrase, Long> implements PhraseService {

    private final PhraseRepository phraseRepository;

    private final ContextRepository contextRepository;

    public PhraseServiceImpl(PhraseRepository phraseRepository, ContextRepository contextRepository) {
        this.phraseRepository = phraseRepository;
        this.contextRepository = contextRepository;
    }

    @Override
    public List<Phrase> getPhrasesByWord(String word, Integer minRank, Integer maxRank, String language) {
        List<Phrase> phrases = phraseRepository.getPhrasesForWord(word, minRank, maxRank, Sort.by("id"));
        phrases = filterByLanguage(phrases, language);
        return phrases;
    }

    @Override
    public List<Phrase> getAllPhrasesWithRank(Integer minRank, Integer maxRank, String language) {
        List<Phrase> phrases = phraseRepository.getPhrasesByRanks(minRank, maxRank, Sort.by("id"));
        phrases = filterByLanguage(phrases, language);
        return phrases;
    }

    @Override
    @Transactional
    public Phrase updatePhrase(PhraseUpdateDto phraseUpdateDto) {

        Phrase phrase = getById(phraseUpdateDto.getId());
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

        return save(phrase);
    }

    private List<Phrase> filterByLanguage(List<Phrase> list, String language) {
        return list.stream().peek(
                ph -> ph.setTranslations(
                        ph.getTranslations().stream()
                                .filter(t -> t.getLanguage().getValue().equals(language))
                                .collect(Collectors.toList())
                )
        ).collect(Collectors.toList());
    }

}
