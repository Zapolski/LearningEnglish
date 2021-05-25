package by.zapolski.english.repository.learning;

import by.zapolski.english.learning.domain.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long>, JpaSpecificationExecutor<Phrase> {

    @Query(
            value = "SELECT * from phrase p " +
                    "WHERE p.value ~* ?1 and (p.rank between ?2 and ?3) ",
            nativeQuery = true)
    List<Phrase> getByPattern(String query, Integer minRank, Integer maxRank);

}
