package by.zapolski.english.dao;

import by.zapolski.english.dao.api.ResourceDao;
import by.zapolski.english.dao.core.ResourceDaoImpl;
import by.zapolski.english.domain.Resource;
import by.zapolski.english.domain.StorageType;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResourceDaoTest extends TestBase {

    private ResourceDao resourceDao;

    private Resource testResource;

    @Before
    public void before() {
        testSessionFactorySetUp();

        resourceDao = new ResourceDaoImpl(sessionFactory);

        testResource = new Resource();
        testResource.setStorageType(StorageType.FILE_SYSTEM);
        testResource.setPath("word-0001.mp3");
        testResource.setSize(123456L);
        testResource.setChecksum("CHECK_SUM");
        testResource.setDuration(123123123L);
    }

    @Test
    public void createAndReturnById() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        resourceDao.save(testResource);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Resource actualResource = resourceDao.getById(testResource.getId());
        transaction.commit();

        assertEquals(testResource, actualResource);
    }

}
