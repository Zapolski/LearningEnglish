package by.zapolski.english.service.learning.core;

import by.zapolski.english.learning.domain.Language;
import by.zapolski.english.repository.learning.LanguageRepository;
import by.zapolski.english.service.learning.api.LanguageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public List<Language> getAll() {
        return languageRepository.findAll();
    }
}
