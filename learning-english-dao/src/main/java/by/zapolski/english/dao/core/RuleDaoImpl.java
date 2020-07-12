package by.zapolski.english.dao.core;

import by.zapolski.english.dao.api.RuleDao;
import by.zapolski.english.domain.Rule;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

public class RuleDaoImpl extends AbstractHibernateDAO<Rule> implements RuleDao {

    public RuleDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Rule.class);
    }

    @Override
    public Rule getByValue(String value) {
        Query query = getCurrentSession().createQuery("from Rule r where r.value=:value");
        query.setParameter("value", value);
        return (Rule) query.uniqueResult();
    }
}
