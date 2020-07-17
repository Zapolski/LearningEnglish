package by.zapolski.english.controller;

import by.zapolski.english.dto.WordDto;
import by.zapolski.english.service.api.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WordController {

    @Autowired
    private WordService wordService;

    @GetMapping("/words/all")
    public List<WordDto> getAll() {
        return wordService.getAll();
    }
}
