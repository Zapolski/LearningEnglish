package by.zapolski.english.dao.core;

import by.zapolski.english.dao.api.TranslationDao;
import by.zapolski.english.domain.Translation;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

public class TranslationDaoImpl extends AbstractHibernateDAO<Translation> implements TranslationDao {

    public TranslationDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Translation.class);
    }

    @Override
    public Translation getByValue(String value) {
        Query query = getCurrentSession().createQuery("from Translation t where t.value=:value");
        query.setParameter("value", value);
        return (Translation) query.uniqueResult();
    }

}
