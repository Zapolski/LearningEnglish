package by.zapolski.english.service.learning.core;

import by.zapolski.english.learning.domain.Context;
import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.domain.Phrase_;
import by.zapolski.english.learning.domain.Translation;
import by.zapolski.english.learning.dto.PhraseDto;
import by.zapolski.english.learning.dto.PhraseSearchDto;
import by.zapolski.english.learning.mapper.PhraseMapper;
import by.zapolski.english.lemma.dto.PhraseUpdateDto;
import by.zapolski.english.repository.learning.ContextRepository;
import by.zapolski.english.repository.learning.PhraseRepository;
import by.zapolski.english.service.CrudBaseServiceImpl;
import by.zapolski.english.service.learning.api.PhraseService;
import by.zapolski.english.service.learning.api.PhraseSpecifications;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PhraseServiceImpl extends CrudBaseServiceImpl<Phrase, Long> implements PhraseService {

    private final PhraseMapper phraseMapper = Mappers.getMapper(PhraseMapper.class);

    private final PhraseRepository phraseRepository;
    private final ContextRepository contextRepository;
    private final PhraseSpecifications phraseSpecifications;


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

    @Override
    public List<PhraseDto> search(PhraseSearchDto searchDto) {
        Specification<Phrase> specification = phraseSpecifications.getSpecification(searchDto);
        List<Phrase> result = phraseRepository.findAll(specification, Sort.by(Sort.Direction.ASC, Phrase_.RANK, Phrase_.ID));
        return result.stream()
                .map(phraseMapper::phraseToDto)
                .collect(Collectors.toList());
    }

}
