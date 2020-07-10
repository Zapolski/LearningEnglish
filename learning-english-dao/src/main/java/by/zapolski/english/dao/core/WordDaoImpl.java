package by.zapolski.english.dao.core;

import by.zapolski.english.dao.api.AbstractHibernateDAO;
import by.zapolski.english.dao.api.WordDao;
import by.zapolski.english.domain.Word;
import org.hibernate.SessionFactory;

public class WordDaoImpl extends AbstractHibernateDAO<Word> implements WordDao {

    public WordDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Word.class);
    }
}
