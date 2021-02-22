package by.zapolski.english.repository.learning;

import by.zapolski.english.learning.domain.Phrase;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {
    long count();

    @Query("select p from Phrase p where p.rank >= :minRank and p.rank <= :maxRank and p.word.value = :wordValue")
    List<Phrase> getPhrasesForWord(
            @Param("wordValue") String wordValue,
            @Param("minRank") Integer minRank,
            @Param("maxRank") Integer maxRank,
            Sort sort
    );

    @Query("select p from Phrase p where p.rank >= :minRank and p.rank <= :maxRank")
    @EntityGraph(value = "phrase-entity-graph")
    List<Phrase> getPhrasesByRanks(
            @Param("minRank") Integer minRank,
            @Param("maxRank") Integer maxRank,
            Sort sort
    );

    @Modifying(clearAutomatically = true)
    @Query("update Phrase p set p.rank = :newRank where p.rank = :currentRank")
    int updateRank(@Param("newRank") Integer newRank, @Param("currentRank") Integer currentRank);

}
