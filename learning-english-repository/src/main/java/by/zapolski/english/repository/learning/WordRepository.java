package by.zapolski.english.repository.learning;

import by.zapolski.english.learning.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    Word getByValue(String value);

    List<Word> getByRank(Integer rank);

    long count();

    @Modifying(clearAutomatically = true)
    @Query("update Word w set w.rank = :newRank where w.rank = :currentRank")
    int updateRank(@Param("newRank") Integer newRank, @Param("currentRank") Integer currentRank);
}
