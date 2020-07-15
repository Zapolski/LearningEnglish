package by.zapolski.english.config;

import by.zapolski.english.dao.api.WordDao;
import by.zapolski.english.dao.core.WordDaoImpl;
import by.zapolski.english.dao.utils.HibernateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public WordDao getWordDao() {
        return new WordDaoImpl(HibernateUtils.getSessionFactory());
    }
}
