package by.zapolski.english.dao;

import by.zapolski.english.dao.api.TranslationDao;
import by.zapolski.english.dao.core.TranslationDaoImpl;
import by.zapolski.english.domain.Translation;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TranslationDaoTest extends TestBase {

    private TranslationDao translationDao;

    private Translation testTranslation;

    @Before
    public void before() {
        testSessionFactorySetUp();

        translationDao = new TranslationDaoImpl(sessionFactory);

        testTranslation = new Translation();
        testTranslation.setValue("привет");
    }

    @Test
    public void createAndReturnById() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        translationDao.save(testTranslation);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Translation actualTranslation = translationDao.getById(testTranslation.getId());
        transaction.commit();

        assertEquals(testTranslation, actualTranslation);
    }

}
