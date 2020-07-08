package by.zapolski.english.dao;

import by.zapolski.english.domain.Word;
import org.hibernate.SessionFactory;

public class WordDaoImpl extends AbstractHibernateDAO<Word> implements WordDao{

    public WordDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Word.class);
    }
}
