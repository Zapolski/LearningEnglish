package by.zapolski.english.dao.api;

import by.zapolski.english.domain.Rule;

import java.util.List;

public interface RuleDao {
    Rule getById(long id);

    Rule getByValue(String value);

    List<Rule> findAll();

    void save(Rule entity);

    Rule update(Rule entity);

    void delete(Rule entity);

    void deleteById(long id);
}
