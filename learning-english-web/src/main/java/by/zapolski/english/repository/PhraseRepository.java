package by.zapolski.english.repository;

import by.zapolski.english.domain.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {
    long count();
}
