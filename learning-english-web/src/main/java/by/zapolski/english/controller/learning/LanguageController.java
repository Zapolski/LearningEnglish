package by.zapolski.english.controller.learning;

import by.zapolski.english.learning.dto.LanguageDto;
import by.zapolski.english.service.learning.api.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @GetMapping("/languages/all")
    public List<LanguageDto> getAll() {
        return languageService.getAll();
    }
}
