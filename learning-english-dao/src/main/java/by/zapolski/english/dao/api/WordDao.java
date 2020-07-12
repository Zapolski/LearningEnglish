package by.zapolski.english.dao.api;

import by.zapolski.english.domain.Word;

import java.util.List;

public interface WordDao {

    Word getById(long id);

    Word getByValue(String value);

    List<Word> getByRank(Integer rank);

    List<Word> findAll();

    void save(Word entity);

    Word update(Word entity);

    void delete(Word entity);

    void deleteById(long id);

}
