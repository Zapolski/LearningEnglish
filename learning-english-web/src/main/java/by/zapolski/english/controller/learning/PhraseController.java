package by.zapolski.english.controller.learning;

import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.dto.PagePhraseDto;
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

    @GetMapping("/phrases/search")
    public PagePhraseDto findAll(PhraseSearchDto searchDto){
        return phraseService.search(searchDto);
    }

    @PutMapping("/phrases/update/{id}")
    public PhraseDto updatePhrase(
            @PathVariable Long id,
            @RequestBody PhraseUpdateDto phraseUpdateDto
    ) {
        return phraseMapper.phraseToDto(phraseService.updatePhrase(phraseUpdateDto));
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
