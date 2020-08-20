package by.zapolski.english.service.learning.core;

import by.zapolski.english.learning.domain.Language;
import by.zapolski.english.service.CrudBaseServiceImpl;
import by.zapolski.english.service.learning.api.LanguageService;
import org.springframework.stereotype.Service;

@Service
public class LanguageServiceImpl extends CrudBaseServiceImpl<Language, Long> implements LanguageService {
}
