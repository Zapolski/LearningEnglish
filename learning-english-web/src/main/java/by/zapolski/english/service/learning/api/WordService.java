package by.zapolski.english.service.learning.api;

import by.zapolski.english.learning.dto.WordDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с английскими словами
 */
@Service
public interface WordService {

    List<WordDto> getAll();
}
