package by.zapolski.english;

import by.zapolski.english.dao.api.WordDao;
import by.zapolski.english.dao.core.WordDaoImpl;
import by.zapolski.english.domain.Word;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WordDaoTest {

    private WordDao wordDao;

    private SessionFactory sessionFactory;

    @Before
    public void before() {
        // setup the session factory
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:test");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.current_session_context_class", "thread");
        configuration.setProperty("show_sql", "true");
        configuration.addAnnotatedClass(Word.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        wordDao = new WordDaoImpl(sessionFactory);
    }

    @Test
    public void createAndReturnWord() {
        Word expectedWord = new Word();
        expectedWord.setValue("hello");
        expectedWord.setRank(500);

        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        wordDao.save(expectedWord);
        transaction.commit();

        currentSession = sessionFactory.getCurrentSession();
        transaction = currentSession.beginTransaction();
        Word actualWords = wordDao.findOne(expectedWord.getId());
        transaction.commit();

        assertEquals(expectedWord, actualWords);
    }

}
