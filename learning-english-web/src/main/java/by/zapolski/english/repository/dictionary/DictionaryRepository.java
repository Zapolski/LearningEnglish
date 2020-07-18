package by.zapolski.english.repository.dictionary;

import by.zapolski.english.dictionary.domain.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
    List<Dictionary> findByValue(String value);
}
