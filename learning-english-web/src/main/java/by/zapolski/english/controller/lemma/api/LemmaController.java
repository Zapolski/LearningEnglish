package by.zapolski.english.controller.lemma.api;

import by.zapolski.english.lemma.domain.Lemma;
import by.zapolski.english.lemma.dto.LemmaDto;
import by.zapolski.english.lemma.dto.LemmaWithSimilarityDto;
import by.zapolski.english.lemma.mapper.LemmaMapper;
import by.zapolski.english.service.lemma.api.LemmaService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class LemmaController {

    private final LemmaMapper lemmaMapper = Mappers.getMapper(LemmaMapper.class);

    private final LemmaService lemmaService;

    public LemmaController(LemmaService lemmaService) {
        this.lemmaService = lemmaService;
    }

    @GetMapping("api/lemma/search")
    public ResponseEntity<List<LemmaWithSimilarityDto>> getSimilarWordsWithAccuracyThreshold(
            @RequestParam String word,
            @RequestParam Integer threshold
    ) {
        return new ResponseEntity<>(
                lemmaService.getSimilarWordsWithAccuracyThreshold(word, threshold),
                HttpStatus.OK
        );
    }

    @PostMapping("api/lemma/create")
    public ResponseEntity<LemmaDto> create(@RequestBody LemmaDto lemmaDto) {
        Lemma lemma = lemmaMapper.dtoToLemma(lemmaDto);
        LemmaDto newLemmaDto = lemmaMapper.lemmaToDto(lemmaService.save(lemma));
        return new ResponseEntity<>(
                newLemmaDto,
                HttpStatus.CREATED
        );
    }
}
