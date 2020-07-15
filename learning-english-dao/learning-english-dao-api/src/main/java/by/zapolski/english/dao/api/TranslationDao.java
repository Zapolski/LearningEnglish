package by.zapolski.english.dao.api;

import by.zapolski.english.domain.Translation;

import java.util.List;

public interface TranslationDao {
    Translation getById(long id);

    Translation getByValue(String value);

    List<Translation> findAll();

    void save(Translation entity);

    Translation update(Translation entity);

    void delete(Translation entity);

    void deleteById(long id);

    Long getCount();
}
