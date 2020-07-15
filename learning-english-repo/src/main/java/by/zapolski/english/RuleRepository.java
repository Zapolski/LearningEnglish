package by.zapolski.english;

import by.zapolski.english.domain.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    Rule getByValue(String value);

    long count();
}
