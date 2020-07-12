package by.zapolski.english.dao;

import by.zapolski.english.dao.api.LanguageDao;
import by.zapolski.english.dao.core.LanguageDaoImpl;
import by.zapolski.english.domain.Language;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LanguageDaoTest extends TestBase {

    private LanguageDao languageDao;

    private Language testLanguage;

    @Before
    public void before() {
        testSessionFactorySetUp();

        languageDao = new LanguageDaoImpl(sessionFactory);

        testLanguage = new Language();
        testLanguage.setValue("Condition One");
    }

    @Test
    public void createAndReturnById() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        languageDao.save(testLanguage);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Language actualLanguage = languageDao.getById(testLanguage.getId());
        transaction.commit();

        assertEquals(testLanguage, actualLanguage);
    }

    @Test
    public void createAndReturnByValue() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        languageDao.save(testLanguage);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Language actualLanguage = languageDao.getByValue(testLanguage.getValue());
        transaction.commit();

        assertEquals(testLanguage, actualLanguage);
    }

}
