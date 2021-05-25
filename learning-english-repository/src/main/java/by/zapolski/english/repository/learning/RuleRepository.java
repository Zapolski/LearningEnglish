package by.zapolski.english.repository.learning;

import by.zapolski.english.learning.domain.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
    Rule getByValue(String value);
}
