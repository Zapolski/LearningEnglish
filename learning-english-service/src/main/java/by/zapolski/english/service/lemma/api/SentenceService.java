package by.zapolski.english.service.lemma.api;

import by.zapolski.english.lemma.dto.SentenceInfoDto;
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

    /**
     * Оборачивает в квадратные скобки имена собственные
     * @param sentence исходное предложение
     * @return результат обработки
     */
    String markProperNouns(String sentence);
}
