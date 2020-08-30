package by.zapolski.english.service.security;


import by.zapolski.english.repository.security.AuthorityRepository;
import by.zapolski.english.repository.security.UserRepository;
import by.zapolski.english.security.domain.User;
import by.zapolski.english.security.dto.UserRegistrationDto;
import by.zapolski.english.service.CrudBaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends CrudBaseServiceImpl<User, Long> implements UserService {

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public boolean registerUser(UserRegistrationDto userRegistrationDto) {
        Optional<User> optionalUserFromDb = userRepository.findByUserName(userRegistrationDto.getUserName());

        if (optionalUserFromDb.isPresent()) {
            return false;
        }

        User user = new User();

        user.setUserName(userRegistrationDto.getUserName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setAuthorities(Collections.singleton(authorityRepository.getByValue("ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationDto.getPassword()));
        user.setActive(true);

        userRepository.save(user);
        return true;
    }
}
