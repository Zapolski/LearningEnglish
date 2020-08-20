package by.zapolski.english.service.learning.api;

import by.zapolski.english.learning.domain.Language;
import by.zapolski.english.service.CrudBaseService;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с языками на которые есть перевод английских фраз-примеров
 */
@Service
public interface LanguageService extends CrudBaseService<Language, Long> {
}
