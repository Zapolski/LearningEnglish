package by.zapolski.english.repository;

import by.zapolski.english.PhraseRepository;
import by.zapolski.english.domain.Phrase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PhraseRepositoryIntegrationTest {

    @Autowired
    private PhraseRepository phraseRepository;

    private Phrase testPhrase;

    @Before
    public void before() {
        testPhrase = new Phrase();
        testPhrase.setValue("You’ll have to wait an hour or two.");
        testPhrase.setRank(1000);
    }

    @Test
    public void createAndReturnById() {
        phraseRepository.save(testPhrase);
        Phrase actualPhrase = phraseRepository.getOne(testPhrase.getId());
        assertEquals(testPhrase, actualPhrase);
    }
}
