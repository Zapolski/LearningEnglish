package by.zapolski.english.repository.learning;

import by.zapolski.english.learning.domain.Context;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContextRepository extends JpaRepository<Context, Long> {

    Context getByValue(String value);

    long count();

}
