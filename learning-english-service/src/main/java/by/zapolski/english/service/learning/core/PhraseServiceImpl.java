package by.zapolski.english.service.learning.core;

import by.zapolski.english.learning.domain.Context;
import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.domain.Phrase_;
import by.zapolski.english.learning.domain.Translation;
import by.zapolski.english.learning.domain.enums.LearningStatus;
import by.zapolski.english.learning.dto.PagePhraseDto;
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
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PhraseServiceImpl extends CrudBaseServiceImpl<Phrase, Long> implements PhraseService {

    private static final float ACTIVE_PERCENT = .3f;
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
        phrase.setLearningStatus(phraseUpdateDto.getLearningStatus());

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
    public PagePhraseDto search(PhraseSearchDto searchDto) {
        Specification<Phrase> specification = phraseSpecifications.getSpecification(searchDto);
        List<Phrase> result = phraseRepository.findAll(specification, Sort.by(Sort.Direction.ASC, Phrase_.RANK, Phrase_.ID));

        return new PagePhraseDto(
                getPhraseDtos(result),
                result.size(),
                getTotalDuration(result)
        );
    }

    @Override
    public PagePhraseDto getByPattern(String query, Integer minRank, Integer maxRank) {
        List<Phrase> result = phraseRepository.getByPattern(query, minRank, maxRank);
        return new PagePhraseDto(
                getPhraseDtos(result),
                result.size(),
                getTotalDuration(result)
        );
    }

    @Override
    public PagePhraseDto getRandomCount(Long count) {
        int activeCount = BigDecimal.valueOf(ACTIVE_PERCENT * count)
                .setScale(0, RoundingMode.CEILING)
                .intValue();
        int newCount = BigDecimal.valueOf((1 - ACTIVE_PERCENT) * count)
                .setScale(0, RoundingMode.FLOOR)
                .intValue();
        List<Phrase> result = phraseRepository.getRandomCount(activeCount, LearningStatus.ACTIVE_LEARNING.name());
        result.addAll(phraseRepository.getRandomCount(newCount, LearningStatus.NEW.name()));
        Collections.shuffle(result);
        return new PagePhraseDto(
                getPhraseDtos(result),
                result.size(),
                getTotalDuration(result)
        );
    }

    @Override
    @Transactional
    public Phrase updateSuccessView(Long id) {
        LocalDateTime now = LocalDateTime.now();
        Phrase phrase = phraseRepository.getOne(id);
        // если дата верификации не установлена, то ставим текущую
        if (phrase.getVerifyDate() == null) {
            phrase.setVerifyDate(now);
        }
        // если дата последнего успешного просмотра не установлена, то
        // ставим текущюю и счетик устанавливаем в 1
        // если успешный просмотр произошел в течении 30 минут от последнего, то игнорируем
        // иначе инкрементируем счетчик и обновляем дату послденего успешного просмотра
        if (phrase.getLastSuccessViewDate() == null) {
            phrase.setLastSuccessViewDate(now);
            phrase.setSuccessViewsCount(1);
        } else if (ChronoUnit.MINUTES.between(phrase.getLastSuccessViewDate(), now) > 30) {
            phrase.setLastSuccessViewDate(now);
            phrase.setSuccessViewsCount(phrase.getSuccessViewsCount() + 1);
        }
        return phraseRepository.saveAndFlush(phrase);
    }

    private List<PhraseDto> getPhraseDtos(List<Phrase> result) {
        return result.stream()
                .map(phraseMapper::phraseToDto)
                .collect(Collectors.toList());
    }

    private String getTotalDuration(List<Phrase> result) {
        return DurationFormatUtils.formatDurationHMS(
                result.stream()
                        .mapToLong(phrase -> phrase.getResource().getDuration())
                        .sum()
        );
    }

}
