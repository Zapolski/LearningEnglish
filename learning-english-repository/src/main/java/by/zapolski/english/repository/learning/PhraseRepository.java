package by.zapolski.english.repository.learning;

import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.repository.dto.IdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long>, JpaSpecificationExecutor<Phrase> {

    @Query(
            value = "SELECT * from phrase p " +
                    "WHERE p.value ~* ?1 and (p.rank between ?2 and ?3) ",
            nativeQuery = true)
    List<Phrase> getByPattern(String query, Integer minRank, Integer maxRank);

    @Query(
            value = "SELECT * from phrase p " +
                    "WHERE learning_status = ?2 " +
                    "order by random() limit ?1",
            nativeQuery = true)
    List<Phrase> getRandomCount(Integer count, String learningStatus);

    @Query(
            value = "SELECT * from phrase p " +
                    "where success_views_count = 0 " +
                    "  and rank between :minRank and :maxRank " +
                    "order by random() limit :limit",
            nativeQuery = true)
    List<Phrase> getNotViewedRandomCountBetweenRanks(
            @Param("minRank") Integer minRank,
            @Param("maxRank") Integer maxRank,
            @Param("limit") Integer limit
    );

    @Query(
            value = "select * " +
                    "from ( " +
                    "         SELECT id, " +
                    "                success_views_count, " +
                    "                rank, " +
                    "                row_number() over (partition by rank order by random()) as row_number " +
                    "         from phrase p) phrases " +
                    "where success_views_count = 0 " +
                    "  and rank between :minRank and :maxRank " +
                    "  and row_number <= :limit",
            nativeQuery = true
    )
    List<IdProjection> getRandomNotViewedPhrasesGroupByRanksWithLimit(
            @Param("minRank") Integer minRank,
            @Param("maxRank") Integer maxRank,
            @Param("limit") Integer limit
    );

    List<Phrase> getByIdIn(List<Long> ids);


    @Query(
            value = "select count(*) from phrase p " +
                    "where success_views_count = 0 " +
                    "  and rank between :minRank and :maxRank ",
            nativeQuery = true
    )
    Integer getNotViewedCountBetweenRanks(
            @Param("minRank") Integer minRank,
            @Param("maxRank") Integer maxRank
    );

    @Query(
            value = "select count(*) from phrase p " +
                    "where success_views_count > 0 " +
                    "  and rank between :minRank and :maxRank ",
            nativeQuery = true
    )
    Integer getViewedCountBetweenRanks(
            @Param("minRank") Integer minRank,
            @Param("maxRank") Integer maxRank
    );

}
