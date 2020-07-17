package by.zapolski.english.repository;

import by.zapolski.english.domain.Word;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WordRepositoryIntegrationTest {

    @Autowired
    private WordRepository wordRepository;

    private Word testWord;

    @Before
    public void before() {
        testWord = new Word();
        testWord.setValue("hello");
        testWord.setRank(500);
    }

    @Test
    public void createAndReturnById() {
        wordRepository.save(testWord);
        Word actualWord = wordRepository.getOne(testWord.getId());
        assertEquals(testWord, actualWord);
    }

    @Test
    public void createAndReturnByValue() {
        wordRepository.save(testWord);
        Word actualWord = wordRepository.getByValue(testWord.getValue());
        assertEquals(testWord, actualWord);
    }

    @Test
    public void createAndReturnByRank() {
        wordRepository.save(testWord);

        testWord = new Word();
        testWord.setValue("word");
        testWord.setRank(500);

        wordRepository.save(testWord);

        List<Word> actualWords = wordRepository.getByRank(testWord.getRank());

        assertEquals(2, actualWords.size());
    }

    @Test
    public void getCountTest() {
        wordRepository.save(testWord);

        testWord = new Word();
        testWord.setValue("word");
        testWord.setRank(500);

        wordRepository.save(testWord);

        Long actualCount = wordRepository.count();

        assertEquals(2L, actualCount);
    }

}
