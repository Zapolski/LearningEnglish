package by.zapolski.english.controller.learning;

import by.zapolski.english.learning.domain.Word;
import by.zapolski.english.learning.dto.WordDto;
import by.zapolski.english.learning.mapper.WordMapper;
import by.zapolski.english.service.learning.api.WordService;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class WordController {

    private final WordMapper wordMapper = Mappers.getMapper(WordMapper.class);

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/words/all")
    public List<WordDto> getAll() {
        List<Word> words = wordService.getAll();
        return words.stream()
                .map(wordMapper::wordToDto)
                .collect(Collectors.toList());
    }
}
