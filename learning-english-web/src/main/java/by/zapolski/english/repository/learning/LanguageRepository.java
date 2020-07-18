package by.zapolski.english.repository.learning;

import by.zapolski.english.learning.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language getByValue(String value);

    long count();
}
