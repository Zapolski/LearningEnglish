package by.zapolski.english.controller.learning;

import by.zapolski.english.learning.dto.CountDto;
import by.zapolski.english.learning.dto.PagePhraseDto;
import by.zapolski.english.learning.dto.PhraseDto;
import by.zapolski.english.learning.dto.PhraseSearchDto;
import by.zapolski.english.learning.mapper.PhraseMapper;
import by.zapolski.english.lemma.dto.PhraseUpdateDto;
import by.zapolski.english.service.learning.api.PhraseService;
import by.zapolski.english.service.lemma.api.SentenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PhraseController {

    private final PhraseMapper phraseMapper;
    private final PhraseService phraseService;
    private final SentenceService sentenceService;

    @GetMapping("/phrases/search")
    public PagePhraseDto findAll(PhraseSearchDto searchDto) {
        // сделано для того, чтобы пробелы учитывались, Spring почему-то при маппинге тримает пробелы
        if (StringUtils.hasText(searchDto.getTextQuery())) {
            List<String> textQueries = Arrays.stream(searchDto.getTextQuery().split(";"))
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toList());
            searchDto.setTextQueries(textQueries);
        }
        PagePhraseDto page = phraseService.search(searchDto);
        return markProperNouns(page);
    }

    @PutMapping("/phrases/update/{id}")
    public PhraseDto updatePhrase(
            @PathVariable Long id,
            @RequestBody PhraseUpdateDto phraseUpdateDto
    ) {
        PhraseDto phrase = phraseMapper.phraseToDto(phraseService.updatePhrase(phraseUpdateDto));
        String source = phrase.getValue();
        String newSource = sentenceService.markProperNouns(source);
        if (!source.equals(newSource)) {
            phrase.setValue(newSource);
            phrase.setModified(true);
        }
        return phrase;
    }

    @PutMapping("/phrases/update/view/{id}")
    public PhraseDto updateSuccessView(@PathVariable Long id) {
        return phraseMapper.phraseToDto(phraseService.updateSuccessView(id));
    }

    @GetMapping("/phrases/pattern/search")
    public PagePhraseDto getByPattern(
            @RequestParam String query,
            @RequestParam(
                    defaultValue = "0",
                    required = false) Integer minRank,
            @RequestParam(
                    defaultValue = "2147483647",
                    required = false) Integer maxRank,
            @RequestParam String language
    ) {
        return phraseService.getByPattern(query, minRank, maxRank);
    }

    @GetMapping("/phrases/search/quick/learning")
    public PagePhraseDto getRandomNotViewedPhrasesGroupByRanksWithLimit(
            @RequestParam(
                    defaultValue = "0",
                    required = false) Integer minRank,
            @RequestParam(
                    defaultValue = "2147483647",
                    required = false) Integer maxRank,
            @RequestParam(
                    defaultValue = "3",
                    required = false) Integer limit
    ) {
        return phraseService.getRandomNotViewedPhrasesGroupByRanksWithLimit(minRank, maxRank, limit);
    }

    @GetMapping("/phrases/random")
    public PagePhraseDto getRandomCount(
            @RequestParam(defaultValue = "0", required = false) Integer minRank,
            @RequestParam(defaultValue = "2147483647", required = false) Integer maxRank,
            @RequestParam(defaultValue = "10", required = false) Integer limit
    ) {
        return markProperNouns(phraseService.getNotViewedRandomCountBetweenRanks(minRank, maxRank, limit));
    }

    @GetMapping("/phrases/counts")
    public CountDto getRandomCount(
            @RequestParam(defaultValue = "0", required = false) Integer minRank,
            @RequestParam(defaultValue = "2147483647", required = false) Integer maxRank
    ) {
        return phraseService.getCountsBetweenRanks(minRank, maxRank);
    }

    private PagePhraseDto markProperNouns(PagePhraseDto page) {
        page.getContent().forEach(phrase -> {
            String source = phrase.getValue();
            String newSource = sentenceService.markProperNouns(source);
            if (!source.equals(newSource)) {
                phrase.setValue(newSource);
                phrase.setModified(true);
            }
        });
        return page;
    }

}
