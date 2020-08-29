package by.zapolski.english.service.security;


import by.zapolski.english.User;
import by.zapolski.english.repository.security.UserRepository;
import by.zapolski.english.service.CrudBaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl extends CrudBaseServiceImpl<User, Long> implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
