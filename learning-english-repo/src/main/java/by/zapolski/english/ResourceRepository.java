package by.zapolski.english;

import by.zapolski.english.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    long count();
}
