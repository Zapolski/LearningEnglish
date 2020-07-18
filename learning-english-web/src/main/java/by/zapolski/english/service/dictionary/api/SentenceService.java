package by.zapolski.english.service.dictionary.api;

import by.zapolski.english.dictionary.dto.SentenceInfoDto;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с английскими предложениями
 */
@Service
public interface SentenceService {

    /**
     * Разбиение предложения на токены, тэги, леммы, части речи и ранги.
     * @param sentence предложение на английском языке
     * @return SentenceInfoDto
     */
    SentenceInfoDto getSentenceInfo(String sentence);

}
