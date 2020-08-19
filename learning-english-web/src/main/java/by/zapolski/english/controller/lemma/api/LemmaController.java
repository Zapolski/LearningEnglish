package by.zapolski.english.controller.lemma.api;

import by.zapolski.english.lemma.dto.LemmaDto;
import by.zapolski.english.lemma.dto.LemmaWithSimilarityDto;
import by.zapolski.english.service.lemma.api.LemmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class LemmaController {

    @Autowired
    private LemmaService lemmaService;

    @GetMapping("api/lemma/search")
    public List<LemmaWithSimilarityDto> getSimilarWordsWithAccuracyThreshold(
            @RequestParam String word,
            @RequestParam Integer threshold
    ) {
        return lemmaService.getSimilarWordsWithAccuracyThreshold(word, threshold);
    }

    @PostMapping("api/lemma/create")
    public LemmaDto create(@RequestBody LemmaDto lemmaDto) {
        return lemmaService.save(lemmaDto);
    }
}
