package by.zapolski.english.repository;

import by.zapolski.english.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language getByValue(String value);

    long count();
}
