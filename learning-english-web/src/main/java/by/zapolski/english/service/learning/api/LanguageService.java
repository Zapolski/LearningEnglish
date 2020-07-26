package by.zapolski.english.service.learning.api;

import by.zapolski.english.learning.dto.LanguageDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с языками на которые есть перевод английских фраз-примеров
 */
@Service
public interface LanguageService {

    List<LanguageDto> getAll();
}
