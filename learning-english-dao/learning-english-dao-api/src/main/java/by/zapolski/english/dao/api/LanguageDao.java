package by.zapolski.english.dao.api;

import by.zapolski.english.domain.Language;

import java.util.List;

public interface LanguageDao {
    Language getById(long id);

    Language getByValue(String value);

    List<Language> findAll();

    void save(Language entity);

    Language update(Language entity);

    void delete(Language entity);

    void deleteById(long id);

    Long getCount();
}
