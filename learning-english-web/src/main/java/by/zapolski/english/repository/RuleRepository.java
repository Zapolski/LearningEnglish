package by.zapolski.english.repository;

import by.zapolski.english.domain.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
    Rule getByValue(String value);

    long count();
}
