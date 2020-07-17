package by.zapolski.english.repository;

import by.zapolski.english.domain.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
    Translation getByValue(String translationValue);

    long count();
}
