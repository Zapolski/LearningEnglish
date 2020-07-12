package by.zapolski.english.dao.api;

import by.zapolski.english.domain.Context;

import java.util.List;

public interface ContextDao {
    Context getById(long id);

    Context getByValue(String value);

    List<Context> findAll();

    void save(Context entity);

    Context update(Context entity);

    void delete(Context entity);

    void deleteById(long id);
}
