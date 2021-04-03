package by.zapolski.english.controller.learning;

import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.dto.PhraseDto;
import by.zapolski.english.learning.dto.PhraseSearchDto;
import by.zapolski.english.learning.mapper.PhraseMapper;
import by.zapolski.english.lemma.dto.PhraseUpdateDto;
import by.zapolski.english.service.learning.api.PhraseService;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PhraseController {

    private final PhraseMapper phraseMapper = Mappers.getMapper(PhraseMapper.class);

    private final PhraseService phraseService;

    public PhraseController(PhraseService phraseService) {
        this.phraseService = phraseService;
    }

    @GetMapping("/phrases/find")
    public List<PhraseDto> findAll(@RequestParam PhraseSearchDto search){
        return Collections.EMPTY_LIST;
    }

    @GetMapping("/phrases/by/word/{word}")
    public List<PhraseDto> getPhrasesForWord(
            @PathVariable String word,
            @RequestParam(
                    defaultValue = "0",
                    required = false) Integer minRank,
            @RequestParam(
                    defaultValue = "2147483647",
                    required = false) Integer maxRank,
            @RequestParam String language
    ) {
        List<Phrase> phrases = phraseService.getPhrasesByWord(word, minRank, maxRank, language);
        return phrases.stream()
                .map(phraseMapper::phraseToDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/phrases/update/{id}")
    public PhraseDto updatePhrase(
            @PathVariable Long id,
            @RequestBody PhraseUpdateDto phraseUpdateDto
    ) {
        return phraseMapper.phraseToDto(phraseService.updatePhrase(phraseUpdateDto));
    }

    @GetMapping("phrases/by/rank")
    public List<PhraseDto> getPhrasesWithRank(
            @RequestParam(
                    defaultValue = "0",
                    required = false) Integer minRank,
            @RequestParam(
                    defaultValue = "2147483647",
                    required = false) Integer maxRank,
            @RequestParam String language
    ) {
        List<Phrase> phrases = phraseService.getAllPhrasesWithRank(minRank, maxRank, language);
        return phrases.stream()
                .map(phraseMapper::phraseToDto)
                .collect(Collectors.toList());
    }

}
