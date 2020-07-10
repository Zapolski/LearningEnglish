package by.zapolski.english.dao.api;

import by.zapolski.english.domain.Word;

import java.util.List;

public interface WordDao {

    Word findOne(long id);

    List<Word> findAll();

    void save(Word entity);

    Word update(Word entity);

    void delete(Word entity);

    void deleteById(long id);

}
