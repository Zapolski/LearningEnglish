package by.zapolski.english.controller.learning;

import by.zapolski.english.dictionary.dto.PhraseUpdateDto;
import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.dto.PhraseDto;
import by.zapolski.english.service.learning.api.PhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PhraseController {

    @Autowired
    private PhraseService phraseService;

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
        return phraseService.getPhrasesByWord(word, minRank, maxRank, language);
    }

    @PutMapping("/phrases/update/{id}")
    public PhraseDto updatePhrase(
            @PathVariable Long id,
            @RequestBody PhraseUpdateDto phraseUpdateDto
    ) {
        return phraseService.updatePhrase(phraseUpdateDto);
    }

    @GetMapping("phrases/by/searchRank")
    public List<PhraseDto> getPhrasesWithRank(
            @RequestParam(
                    defaultValue = "0",
                    required = false) Integer minRank,
            @RequestParam(
                    defaultValue = "2147483647",
                    required = false) Integer maxRank,
            @RequestParam String language
    ) {
        return phraseService.getAllPhrasesWithRank(minRank, maxRank, language);
    }


}
