package by.zapolski.english.repository;

import by.zapolski.english.learning.domain.Resource;
import by.zapolski.english.learning.domain.enums.StorageType;
import by.zapolski.english.repository.learning.ResourceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ResourceRepositoryIntegrationTest {

    @Autowired
    private ResourceRepository resourceRepository;

    private Resource testResource;

    @Before
    public void before() {
        testResource = new Resource();
        testResource.setStorageType(StorageType.FILE_SYSTEM);
        testResource.setPath("word-0001.mp3");
        testResource.setSize(123456L);
        testResource.setChecksum("CHECK_SUM");
        testResource.setDuration(123123123L);
    }

    @Test
    public void createAndReturnById() {
        resourceRepository.save(testResource);
        Resource actualResource = resourceRepository.getOne(testResource.getId());
        assertEquals(testResource, actualResource);
    }
}
