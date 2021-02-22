package by.zapolski.english.repository.dictionary;

import by.zapolski.english.lemma.domain.Lemma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LemmaRepository extends JpaRepository<Lemma, Long> {
    List<Lemma> findByValue(String value);

    List<Lemma> getByRank(Integer rank);

    List<Lemma> findByValueIn(List<String> values);

    @Modifying(clearAutomatically = true)
    @Query("update Lemma l set l.rank = :newRank where l.rank = :currentRank")
    int updateRank(@Param("newRank") Integer newRank, @Param("currentRank") Integer currentRank);
}
