package by.zapolski.english.repository;

import by.zapolski.english.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    Word getByValue(String value);

    List<Word> getByRank(Integer rank);

    long count();
}
