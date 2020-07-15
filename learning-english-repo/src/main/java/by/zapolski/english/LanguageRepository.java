package by.zapolski.english;

import by.zapolski.english.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language getByValue(String value);

    long count();
}
