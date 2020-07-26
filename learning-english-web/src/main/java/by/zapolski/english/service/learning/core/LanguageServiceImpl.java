package by.zapolski.english.service.learning.core;

import by.zapolski.english.learning.domain.Language;
import by.zapolski.english.learning.dto.LanguageDto;
import by.zapolski.english.learning.mapper.LanguageMapper;
import by.zapolski.english.repository.learning.LanguageRepository;
import by.zapolski.english.service.learning.api.LanguageService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    private LanguageMapper languageMapper = Mappers.getMapper(LanguageMapper.class);

    @Override
    public List<LanguageDto> getAll() {

        List<Language> words = languageRepository.findAll();

        return words.stream()
                .map(language -> languageMapper.languageToDto(language))
                .collect(Collectors.toList());
    }
}
