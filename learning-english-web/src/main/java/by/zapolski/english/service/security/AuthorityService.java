package by.zapolski.english.service.security;

import by.zapolski.english.security.domain.Authority;
import by.zapolski.english.service.CrudBaseService;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с ролями
 */
@Service
public interface AuthorityService extends CrudBaseService<Authority, Long> {
}
