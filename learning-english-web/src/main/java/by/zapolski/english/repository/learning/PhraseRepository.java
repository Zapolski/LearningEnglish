package by.zapolski.english.repository.learning;

import by.zapolski.english.learning.domain.Phrase;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {
    long count();

    //select * from phrase p where (p.rank between 0 and 610010) and p.word_id = (select id from word w where w.value = 'bag');
    //@Query("SELECT a FROM Locality a INNER JOIN a.city c WHERE c.cityId = :cityId")
    @Query("select p from Phrase p where p.rank >= :minRank and p.rank <= :maxRank and p.word.value = :wordValue ")
    List<Phrase> getPhrasesForWord(
            @Param("wordValue") String wordValue,
            @Param("minRank") Integer minRank,
            @Param("maxRank") Integer maxRank,
            Sort sort);

}
