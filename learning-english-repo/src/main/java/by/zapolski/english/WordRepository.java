package by.zapolski.english;

import by.zapolski.english.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {

    Word getByValue(String value);

    List<Word> getByRank(Integer rank);

    long count();
}
