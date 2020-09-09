package by.zapolski.english.service;

import java.util.List;

public interface CrudBaseService<T, ID> {

    List<T> getAll();

    T getById(ID id);

    void delete(T t);

    void deleteById(ID id);

    T save(T t);

}
