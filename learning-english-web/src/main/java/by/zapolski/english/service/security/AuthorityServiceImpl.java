package by.zapolski.english.service.security;


import by.zapolski.english.Authority;
import by.zapolski.english.service.CrudBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl extends CrudBaseServiceImpl<Authority, Long> implements AuthorityService {
}
