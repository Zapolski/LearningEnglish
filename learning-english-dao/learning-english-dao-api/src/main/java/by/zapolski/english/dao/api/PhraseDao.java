package by.zapolski.english.dao.api;

import by.zapolski.english.domain.Phrase;

import java.util.List;

public interface PhraseDao {
    Phrase getById(long id);

    List<Phrase> findAll();

    void save(Phrase entity);

    Phrase update(Phrase entity);

    void delete(Phrase entity);

    void deleteById(long id);

    Long getCount();
}
