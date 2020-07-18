package by.zapolski.english.service.learning.api;

import org.springframework.stereotype.Service;

/**
 * Сервис для работы с БД изучения английского
 */
@Service
public interface DbLoaderService {

    /**
     * Очищает базу, заполняет её значениями из инициализационного файла
     */
    void resetDatabase();
}
