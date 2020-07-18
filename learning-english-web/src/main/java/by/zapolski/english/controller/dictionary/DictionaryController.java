package by.zapolski.english.controller.dictionary;

import by.zapolski.english.dictionary.domain.Dictionary;
import by.zapolski.english.dictionary.dto.DictionaryDto;
import by.zapolski.english.dictionary.dto.DictionaryWithSimilarityDto;
import by.zapolski.english.dictionary.mapper.DictionaryMapper;
import by.zapolski.english.service.dictionary.api.DictionaryService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class DictionaryController {

    private DictionaryMapper dictionaryMapper = Mappers.getMapper(DictionaryMapper.class);

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("dictionary/frequency")
    public List<DictionaryWithSimilarityDto> getSimilarWordsWithAccuracyThreshold(
            @RequestParam String word,
            @RequestParam Integer threshold
    ) {
        return dictionaryService.getSimilarWordsWithAccuracyThreshold(word, threshold);
    }

    @PostMapping("dictionary/create")
    public DictionaryDto create(@RequestBody DictionaryDto dictionaryDto) {

        Dictionary dictionary = dictionaryService.save(dictionaryDto);

        return dictionaryMapper.dictionaryToDto(dictionary);
    }
}
