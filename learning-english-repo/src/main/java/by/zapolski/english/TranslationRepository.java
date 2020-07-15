package by.zapolski.english;

import by.zapolski.english.domain.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRepository extends JpaRepository<Translation, Long> {
    Translation getByValue(String translationValue);

    long count();
}
