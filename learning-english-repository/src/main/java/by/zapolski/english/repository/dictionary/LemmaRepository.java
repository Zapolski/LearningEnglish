package by.zapolski.english.repository.dictionary;

import by.zapolski.english.lemma.domain.Lemma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LemmaRepository extends JpaRepository<Lemma, Long> {
    List<Lemma> findByValue(String value);

    List<Lemma> getByRank(Integer rank);
}
