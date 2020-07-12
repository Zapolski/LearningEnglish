package by.zapolski.english.dao;

import by.zapolski.english.dao.api.PhraseDao;
import by.zapolski.english.dao.core.PhraseDaoImpl;
import by.zapolski.english.domain.Phrase;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhraseDaoTest extends TestBase {

    private PhraseDao PhraseDao;

    private Phrase testPhrase;

    @Before
    public void before() {
        testSessionFactorySetUp();

        PhraseDao = new PhraseDaoImpl(sessionFactory);

        testPhrase = new Phrase();
        testPhrase.setValue("You’ll have to wait an hour or two.");
        testPhrase.setRank(1000);
    }

    @Test
    public void createAndReturnById() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        PhraseDao.save(testPhrase);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Phrase actualPhrase = PhraseDao.getById(testPhrase.getId());
        transaction.commit();

        assertEquals(testPhrase, actualPhrase);
    }

}
