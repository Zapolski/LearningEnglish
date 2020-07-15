package by.zapolski.english;

import by.zapolski.english.domain.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhraseRepository extends JpaRepository<Phrase, Long> {
    long count();
}
