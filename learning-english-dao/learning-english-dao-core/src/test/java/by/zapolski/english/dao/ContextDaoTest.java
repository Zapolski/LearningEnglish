package by.zapolski.english.dao;

import by.zapolski.english.dao.api.ContextDao;
import by.zapolski.english.dao.core.ContextDaoImpl;
import by.zapolski.english.domain.Context;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContextDaoTest extends TestBase {

    private ContextDao contextDao;

    private Context testContext;

    @Before
    public void before() {
        testSessionFactorySetUp();

        contextDao = new ContextDaoImpl(sessionFactory);

        testContext = new Context("used in some phrases that say how much of something there is");
    }

    @Test
    public void createAndReturnById() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        contextDao.save(testContext);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Context actualContext = contextDao.getById(testContext.getId());
        transaction.commit();

        assertEquals(testContext, actualContext);
    }

    @Test
    public void createAndReturnByValue() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        contextDao.save(testContext);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Context actualContext = contextDao.getByValue(testContext.getValue());
        transaction.commit();

        assertEquals(testContext, actualContext);
    }

}
