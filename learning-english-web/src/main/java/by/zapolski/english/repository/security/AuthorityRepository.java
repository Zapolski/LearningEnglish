package by.zapolski.english.repository.security;

import by.zapolski.english.security.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority getByValue(String value);
}
