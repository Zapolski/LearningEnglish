package by.zapolski.english.controller.learning;

import by.zapolski.english.learning.domain.Language;
import by.zapolski.english.learning.dto.LanguageDto;
import by.zapolski.english.learning.mapper.LanguageMapper;
import by.zapolski.english.service.learning.api.LanguageService;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LanguageController {

    private final LanguageMapper languageMapper = Mappers.getMapper(LanguageMapper.class);

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping("/languages/all")
    public List<LanguageDto> getAll() {
        List<Language> languages = languageService.getAll();
        return languages.stream()
                .map(languageMapper::languageToDto)
                .collect(Collectors.toList());
    }
}
