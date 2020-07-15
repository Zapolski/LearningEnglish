package by.zapolski.english.dao;

import by.zapolski.english.dao.api.WordDao;
import by.zapolski.english.dao.core.WordDaoImpl;
import by.zapolski.english.domain.Word;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class WordDaoTest extends TestBase {

    private WordDao wordDao;

    private Word testWord;

    @Before
    public void before() {
        testSessionFactorySetUp();

        wordDao = new WordDaoImpl(sessionFactory);

        testWord = new Word();
        testWord.setValue("hello");
        testWord.setRank(500);
    }

    @Test
    public void createAndReturnById() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        wordDao.save(testWord);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Word actualWord = wordDao.getById(testWord.getId());
        transaction.commit();

        assertEquals(testWord, actualWord);
    }

    @Test
    public void createAndReturnByValue() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        wordDao.save(testWord);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Word actualWord = wordDao.getByValue(testWord.getValue());
        transaction.commit();

        assertEquals(testWord, actualWord);
    }

    @Test
    public void createAndReturnByRank() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        wordDao.save(testWord);
        transaction.commit();

        testWord = new Word();
        testWord.setValue("word");
        testWord.setRank(500);

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        wordDao.save(testWord);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        List<Word> actualWords = wordDao.getByRank(testWord.getRank());
        transaction.commit();

        assertEquals(2, actualWords.size());
    }

    @Test
    public void getCountTest() {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        wordDao.save(testWord);
        transaction.commit();

        testWord = new Word();
        testWord.setValue("word");
        testWord.setRank(500);

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        wordDao.save(testWord);
        transaction.commit();

        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Long actualCount = wordDao.getCount();
        transaction.commit();

        assertEquals(Long.valueOf(2L),actualCount);

    }

}
