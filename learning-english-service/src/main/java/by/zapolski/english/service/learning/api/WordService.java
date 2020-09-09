package by.zapolski.english.service.learning.api;

import by.zapolski.english.learning.domain.Word;
import by.zapolski.english.service.CrudBaseService;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с английскими словами
 */
@Service
public interface WordService extends CrudBaseService<Word, Long> {
}
