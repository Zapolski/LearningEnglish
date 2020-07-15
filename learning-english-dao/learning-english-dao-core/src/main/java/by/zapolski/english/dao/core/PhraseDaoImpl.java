package by.zapolski.english.dao.core;

import by.zapolski.english.dao.api.PhraseDao;
import by.zapolski.english.domain.Phrase;
import org.hibernate.SessionFactory;

public class PhraseDaoImpl extends AbstractHibernateDAO<Phrase> implements PhraseDao {

    public PhraseDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Phrase.class);
    }

}
