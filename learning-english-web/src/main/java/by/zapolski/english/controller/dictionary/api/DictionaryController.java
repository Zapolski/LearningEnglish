package by.zapolski.english.controller.dictionary.api;

import by.zapolski.english.dictionary.dto.DictionaryDto;
import by.zapolski.english.dictionary.dto.DictionaryWithSimilarityDto;
import by.zapolski.english.service.dictionary.api.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("api/dictionary/search")
    public List<DictionaryWithSimilarityDto> getSimilarWordsWithAccuracyThreshold(
            @RequestParam String word,
            @RequestParam Integer threshold
    ) {
        return dictionaryService.getSimilarWordsWithAccuracyThreshold(word, threshold);
    }

    @PostMapping("api/dictionary/create")
    public DictionaryDto create(@RequestBody DictionaryDto dictionaryDto) {
        return dictionaryService.save(dictionaryDto);
    }
}
