package by.zapolski.english.repository;

import by.zapolski.english.learning.domain.Language;
import by.zapolski.english.repository.learning.LanguageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LanguageRepositoryIntegrationTest {

    @Autowired
    private LanguageRepository languageRepository;

    private Language testLanguage;

    @Before
    public void before() {
        testLanguage = new Language();
        testLanguage.setValue("Condition One");
    }

    @Test
    public void createAndReturnById() {
        languageRepository.save(testLanguage);
        Language actualLanguage = languageRepository.getOne(testLanguage.getId());
        assertEquals(testLanguage, actualLanguage);
    }

    @Test
    public void createAndReturnByValue() {
        languageRepository.save(testLanguage);
        Language actualLanguage = languageRepository.getByValue(testLanguage.getValue());
        assertEquals(testLanguage, actualLanguage);
    }
}
