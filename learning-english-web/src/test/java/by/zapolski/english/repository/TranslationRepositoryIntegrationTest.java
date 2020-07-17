package by.zapolski.english.repository;

import by.zapolski.english.domain.Translation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TranslationRepositoryIntegrationTest {

    @Autowired
    private TranslationRepository translationRepository;

    private Translation testTranslation;

    @Before
    public void before() {
        testTranslation = new Translation();
        testTranslation.setValue("привет");
    }

    @Test
    public void createAndReturnById() {
        translationRepository.save(testTranslation);
        Translation actualTranslation = translationRepository.getOne(testTranslation.getId());
        assertEquals(testTranslation, actualTranslation);
    }
}
