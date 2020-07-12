package by.zapolski.english.dao;

import by.zapolski.english.dao.api.*;
import by.zapolski.english.dao.core.*;
import by.zapolski.english.domain.*;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntegrationPhraseTest extends TestBase {

    private PhraseDao phraseDao;
    private LanguageDao languageDao;
    private WordDao wordDao;
    private TranslationDao translationDao;
    private ContextDao contextDao;
    private ResourceDao resourceDao;
    private RuleDao ruleDao;

    private Context testContext;
    private Word testWord;

    private Language testLanguageRU;
    private Language testLanguageBLR;

    private Translation testTranslationRU;
    private Translation testTranslationBLR;
    private List<Translation> translations = new ArrayList<>();

    private Phrase testPhrase;
    private Resource testResource;

    private Rule testRule1;
    private Rule testRule2;
    private List<Rule> rules = new ArrayList<>();

    @Before
    public void before() {
        testSessionFactorySetUp();

        phraseDao = new PhraseDaoImpl(sessionFactory);
        languageDao = new LanguageDaoImpl(sessionFactory);
        wordDao = new WordDaoImpl(sessionFactory);
        translationDao = new TranslationDaoImpl(sessionFactory);
        contextDao = new ContextDaoImpl(sessionFactory);
        resourceDao = new ResourceDaoImpl(sessionFactory);
        ruleDao = new RuleDaoImpl(sessionFactory);

        testWord = new Word("a", 5);
        testContext = new Context("used in some phrases that say how much of something there is");

        testLanguageRU = new Language("RU");
        testLanguageBLR = new Language("BLR");

        testTranslationRU = new Translation("Вы доставили много хлопот.");
        testTranslationRU.setLanguage(testLanguageRU);
        testTranslationBLR = new Translation("Ад вас шмат турбот");
        testTranslationBLR.setLanguage(testLanguageBLR);
        translations.add(testTranslationRU);
        translations.add(testTranslationBLR);

        testRule1 = new Rule("Indefinite Article");
        testRule2 = new Rule("Past Perfect");
        rules.add(testRule1);
        rules.add(testRule2);

        testResource = new Resource(
                StorageType.FILE_SYSTEM,
                "a-0001.mp3",
                123456L,
                "SOME_CHECK_SUM",
                123123123L
        );

        testPhrase = new Phrase("You have caused a great deal of trouble.", 500);
        testPhrase.setWord(testWord);
        testPhrase.setResource(testResource);
        testPhrase.setContext(testContext);
        testPhrase.setRules(rules);
        testPhrase.setTranslations(translations);

        testTranslationRU.setPhrase(testPhrase);
        testTranslationBLR.setPhrase(testPhrase);
    }

    @Test
    public void createAndReturnByIdFullPhrase() {

        //сохряняем полностью заполненную фразу-пример в пустую базу
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        phraseDao.save(testPhrase);
        transaction.commit();

        //читаем сохраненную фразу-пример из базы по Id
        transaction = sessionFactory.getCurrentSession().beginTransaction();
        Phrase actualPhrase = phraseDao.getById(testPhrase.getId());
        // проверяем её на Equals с исходной
        assertEquals(testPhrase, actualPhrase);

        // читаем из базы все сопутствующие объекты проверяем их на Equals с исходными
        Word actualWord = wordDao.getById(actualPhrase.getWord().getId());
        assertEquals(testWord, actualWord);

        Context actualContext = contextDao.getById(actualPhrase.getContext().getId());
        assertEquals(testContext, actualContext);

        Resource actualResource = resourceDao.getById(actualPhrase.getResource().getId());
        assertEquals(testResource, actualResource);

        List<Rule> actualRules = actualPhrase.getRules();
        assertEquals(rules.size(), actualRules.size());
        for (Rule actualRule: actualRules){
            Rule dbRule = ruleDao.getById(actualRule.getId());
            assertTrue(rules.contains(dbRule));
        }

        Set<Language> actualLanguageSet = new HashSet<>();

        List<Translation> actualTranslations = actualPhrase.getTranslations();
        assertEquals(translations.size(), actualTranslations.size());
        for (Translation actualTranslation: actualTranslations){
            Translation dbTranslation = translationDao.getById(actualTranslation.getId());
            assertTrue(translations.contains(dbTranslation));

            actualLanguageSet.add(languageDao.getById(actualTranslation.getLanguage().getId()));
        }

        Set<Language> expectedLanguageSet = new HashSet<>();
        expectedLanguageSet.add(testLanguageRU);
        expectedLanguageSet.add(testLanguageBLR);
        assertEquals(expectedLanguageSet,actualLanguageSet);

        transaction.commit();
    }
}
