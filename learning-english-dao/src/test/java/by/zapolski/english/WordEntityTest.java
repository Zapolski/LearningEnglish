package by.zapolski.english;

import by.zapolski.english.dao.WordDaoImpl;
import by.zapolski.english.domain.Word;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class WordEntityTest {

    private WordDaoImpl wordDao;

    @Before
    public void before() {
        // setup the session factory
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:test");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.current_session_context_class","thread");
        configuration.setProperty("show_sql","true");
        configuration.addAnnotatedClass(Word.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        wordDao = new WordDaoImpl(sessionFactory);
    }

    @Test
    public void createAndReturnWord(){
        Word word = new Word();
        word.setValue("hello");
        word.setRank(500);

        wordDao.openCurrentSessionWithTransaction();
        wordDao.save(word);
        wordDao.closeCurrentSessionWithTransaction();

        wordDao.openCurrentSession();
        List<Word> words = wordDao.findAll();
        wordDao.closeCurrentSession();

        assertEquals(1, words.size());
    }
}
