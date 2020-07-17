package by.zapolski.english.repository;

import by.zapolski.english.domain.Context;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContextRepository extends JpaRepository<Context, Long> {

    Context getByValue(String value);

    long count();

}
