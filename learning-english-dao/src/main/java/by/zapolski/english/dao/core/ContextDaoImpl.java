package by.zapolski.english.dao.core;

import by.zapolski.english.dao.api.ContextDao;
import by.zapolski.english.domain.Context;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

public class ContextDaoImpl extends AbstractHibernateDAO<Context> implements ContextDao {

    public ContextDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Context.class);
    }

    @Override
    public Context getByValue(String value) {
        Query query = getCurrentSession().createQuery("from Context c where c.value=:value");
        query.setParameter("value", value);
        return (Context) query.uniqueResult();
    }
}
