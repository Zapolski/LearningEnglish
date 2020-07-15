package by.zapolski.english.dao;

import by.zapolski.english.dao.api.RuleDao;
import by.zapolski.english.dao.core.RuleDaoImpl;
import by.zapolski.english.domain.Rule;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RuleDaoTest extends TestBase {

    private RuleDao ruleDao;

    private Rule testRule;

    @Before
    public void before() {
        testSessionFactorySetUp();

        ruleDao = new RuleDaoImpl(sessionFactory);

        testRule = new Rule();
        testRule.setValue("Condition One");
    }

    @Test
    public void createAndReturnById() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        ruleDao.save(testRule);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Rule actualRule = ruleDao.getById(testRule.getId());
        assertEquals(testRule, actualRule);
        transaction.commit();
    }

    @Test
    public void createAndReturnByValue() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        ruleDao.save(testRule);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Rule actualRule = ruleDao.getByValue(testRule.getValue());
        assertEquals(testRule, actualRule);
        transaction.commit();
    }

}
