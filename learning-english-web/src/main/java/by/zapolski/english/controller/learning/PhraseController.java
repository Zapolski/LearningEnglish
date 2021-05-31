package by.zapolski.english.controller.learning;

import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.dto.PagePhraseDto;
import by.zapolski.english.learning.dto.PhraseDto;
import by.zapolski.english.learning.dto.PhraseSearchDto;
import by.zapolski.english.learning.mapper.PhraseMapper;
import by.zapolski.english.lemma.dto.PhraseUpdateDto;
import by.zapolski.english.service.learning.api.PhraseService;
import by.zapolski.english.service.lemma.api.SentenceService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PhraseController {

    private final PhraseMapper phraseMapper = Mappers.getMapper(PhraseMapper.class);

    @Autowired
    private PhraseService phraseService;

    @Autowired
    private SentenceService sentenceService;

    @GetMapping("/phrases/search")
    public PagePhraseDto findAll(PhraseSearchDto searchDto){
        PagePhraseDto page = phraseService.search(searchDto);
        page.getContent().forEach(phrase -> {
            String source = phrase.getValue();
            String newSource = sentenceService.markProperNouns(source);
            if (!source.equals(newSource)){
                phrase.setValue(newSource);
                phrase.setModified(true);
            }
        });
        return page;
    }

    @PutMapping("/phrases/update/{id}")
    public PhraseDto updatePhrase(
            @PathVariable Long id,
            @RequestBody PhraseUpdateDto phraseUpdateDto
    ) {
        PhraseDto phrase = phraseMapper.phraseToDto(phraseService.updatePhrase(phraseUpdateDto));
        String source = phrase.getValue();
        String newSource = sentenceService.markProperNouns(source);
        if (!source.equals(newSource)){
            phrase.setValue(newSource);
            phrase.setModified(true);
        }
        return phrase;
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

}
