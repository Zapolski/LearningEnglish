package by.zapolski.english.dao.core;

import by.zapolski.english.dao.api.WordDao;
import by.zapolski.english.domain.Word;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

public class WordDaoImpl extends AbstractHibernateDAO<Word> implements WordDao {

    public WordDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Word.class);
    }

    @Override
    public Word getByValue(String value) {
        Query query = getCurrentSession().createQuery("from Word w where w.value=:value");
        query.setParameter("value", value);
        return (Word)query.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Word> getByRank(Integer rank) {
        Query query = getCurrentSession().createQuery("from Word w where w.rank=:rank");
        query.setParameter("rank", rank);
        return query.list();
    }
}
