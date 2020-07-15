package by.zapolski.english.dao.core;

import by.zapolski.english.dao.api.ResourceDao;
import by.zapolski.english.domain.Resource;
import org.hibernate.SessionFactory;

public class ResourceDaoImpl extends AbstractHibernateDAO<Resource> implements ResourceDao {

    public ResourceDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Resource.class);
    }

}
