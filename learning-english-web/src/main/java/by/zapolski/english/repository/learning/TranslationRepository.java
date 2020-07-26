package by.zapolski.english.repository.learning;

import by.zapolski.english.learning.domain.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
    Translation getByValue(String translationValue);

    Translation tegByLanguageValueAndPhraseId(String languageValue, Long phraseId);

    long count();
}
