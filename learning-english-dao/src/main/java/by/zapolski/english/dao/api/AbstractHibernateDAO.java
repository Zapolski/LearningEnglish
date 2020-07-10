package by.zapolski.english.dao.api;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class AbstractHibernateDAO<T extends Serializable> {

    private Class<T> clazz;

    private SessionFactory sessionFactory;

    protected AbstractHibernateDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    protected void setClazz(Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    public T findOne(long id) {
        return getCurrentSession().get(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return getCurrentSession().createQuery("from " + clazz.getName()).list();
    }

    public void save(T entity) {
        getCurrentSession().persist(entity);
    }

    @SuppressWarnings("unchecked")
    public T update(T entity) {
        return (T) getCurrentSession().merge(entity);
    }

    public void delete(T entity) {
        getCurrentSession().delete(entity);
    }

    public void deleteById(long id) {
        final T entity = findOne(id);
        delete(entity);
    }

}
