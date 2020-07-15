package by.zapolski.english.dao.core;

import by.zapolski.english.dao.api.LanguageDao;
import by.zapolski.english.domain.Language;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

public class LanguageDaoImpl extends AbstractHibernateDAO<Language> implements LanguageDao {

    public LanguageDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Language.class);
    }

    @Override
    public Language getByValue(String value) {
        Query query = getCurrentSession().createQuery("from Language l where l.value=:value");
        query.setParameter("value", value);
        return (Language) query.uniqueResult();
    }
}
