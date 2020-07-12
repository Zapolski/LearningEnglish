package by.zapolski.english.dao.core;

import by.zapolski.english.dao.api.TranslationDao;
import by.zapolski.english.domain.Translation;
import org.hibernate.SessionFactory;

public class TranslationDaoImpl extends AbstractHibernateDAO<Translation> implements TranslationDao {

    public TranslationDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Translation.class);
    }

}
