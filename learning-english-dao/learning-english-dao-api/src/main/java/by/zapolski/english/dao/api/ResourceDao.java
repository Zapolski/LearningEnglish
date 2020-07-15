package by.zapolski.english.dao.api;

import by.zapolski.english.domain.Resource;

import java.util.List;

public interface ResourceDao {
    Resource getById(long id);

    List<Resource> findAll();

    void save(Resource entity);

    Resource update(Resource entity);

    void delete(Resource entity);

    void deleteById(long id);

    Long getCount();
}
