package by.zapolski.english.service.security;

import by.zapolski.english.User;
import by.zapolski.english.service.CrudBaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для работы с пользователями
 */
@Service
public interface UserService extends CrudBaseService<User, Long> {
    Optional<User> findByUserName(String userName);
}
