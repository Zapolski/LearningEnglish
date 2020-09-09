package by.zapolski.english.service.lemma.api;

import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Сервис для работы с английским текстом
 */
@Service
public interface TextService {

    /**
     * Получение набора уникальных лемм, употребляемых в тексте
     * @param text английский текст
     * @return набор уникальных лемм
     */
    Set<String> getUniqueLemmasFromText(String text);

}
