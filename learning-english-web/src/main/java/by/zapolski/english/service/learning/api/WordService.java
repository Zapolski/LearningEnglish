package by.zapolski.english.service.learning.api;

import by.zapolski.english.learning.domain.Word;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с английскими словами
 */
@Service
public interface WordService {
    List<Word> getAll();
}
