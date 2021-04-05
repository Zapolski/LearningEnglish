package by.zapolski.english.service.learning.api;

import org.springframework.stereotype.Service;

/**
 * Сервис для работы с БД изучения английского
 */
@Service
public interface DbService {

    /**
     * Очищает базу, заполняет её значениями из инициализационного файла
     */
    void resetDatabase();

    /**
     * Резервная копия текущего состояния базы в Excel файл
     */
    void backup();
}
