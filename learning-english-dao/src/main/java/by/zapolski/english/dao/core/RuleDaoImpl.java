package by.zapolski.english.dao.core;

import by.zapolski.english.dao.api.AbstractHibernateDAO;
import by.zapolski.english.dao.api.RuleDao;
import by.zapolski.english.domain.Rule;
import org.hibernate.SessionFactory;

public class RuleDaoImpl extends AbstractHibernateDAO<Rule> implements RuleDao {

    public RuleDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Rule.class);
    }
}
