package by.zapolski.english.repository;

import by.zapolski.english.*;
import by.zapolski.english.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryIntegrationTest {

    @Autowired
    private PhraseRepository phraseRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private RuleRepository ruleRepository;

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
        phraseRepository.save(testPhrase);

        //читаем сохраненную фразу-пример из базы по Id
        Phrase actualPhrase = phraseRepository.getOne(testPhrase.getId());
        // проверяем её на Equals с исходной
        assertEquals(testPhrase, actualPhrase);

        // читаем из базы все сопутствующие объекты проверяем их на Equals с исходными
        Word actualWord = wordRepository.getOne(actualPhrase.getWord().getId());
        assertEquals(testWord, actualWord);

        Context actualContext = contextRepository.getOne(actualPhrase.getContext().getId());
        assertEquals(testContext, actualContext);

        Resource actualResource = resourceRepository.getOne(actualPhrase.getResource().getId());
        assertEquals(testResource, actualResource);

        List<Rule> actualRules = actualPhrase.getRules();
        assertEquals(rules.size(), actualRules.size());
        for (Rule actualRule: actualRules){
            Rule dbRule = ruleRepository.getOne(actualRule.getId());
            assertTrue(rules.contains(dbRule));
        }

        Set<Language> actualLanguageSet = new HashSet<>();

        List<Translation> actualTranslations = actualPhrase.getTranslations();
        assertEquals(translations.size(), actualTranslations.size());
        for (Translation actualTranslation: actualTranslations){
            Translation dbTranslation = translationRepository.getOne(actualTranslation.getId());
            assertTrue(translations.contains(dbTranslation));

            actualLanguageSet.add(languageRepository.getOne(actualTranslation.getLanguage().getId()));
        }

        Set<Language> expectedLanguageSet = new HashSet<>();
        expectedLanguageSet.add(testLanguageRU);
        expectedLanguageSet.add(testLanguageBLR);
        assertEquals(expectedLanguageSet,actualLanguageSet);
    }

    @Test
    public void createAndRemoveFullPhrase() {

        //сохряняем полностью заполненную фразу-пример в пустую базу
        phraseRepository.save(testPhrase);

        //читаем сохраненную фразу-пример из базы по Id
        Phrase actualPhrase = phraseRepository.getOne(testPhrase.getId());
        // проверяем её на Equals с исходной
        assertEquals(testPhrase, actualPhrase);

        // проверяем наличие/отсутствие необходимой информации
        assertEquals(1, phraseRepository.count());
        assertEquals(2, translationRepository.count());
        assertEquals(1, resourceRepository.count());
        assertEquals(2, languageRepository.count());
        assertEquals(2, ruleRepository.count());
        assertEquals(1, wordRepository.count());
        assertEquals(1, contextRepository.count());

        // читаем из базы все сопутствующие объекты проверяем их на Equals с исходными
        Word actualWord = wordRepository.getOne(actualPhrase.getWord().getId());
        assertEquals(testWord, actualWord);

        Context actualContext = contextRepository.getOne(actualPhrase.getContext().getId());
        assertEquals(testContext, actualContext);

        Resource actualResource = resourceRepository.getOne(actualPhrase.getResource().getId());
        assertEquals(testResource, actualResource);

        List<Rule> actualRules = actualPhrase.getRules();
        assertEquals(rules.size(), actualRules.size());
        for (Rule actualRule: actualRules){
            Rule dbRule = ruleRepository.getOne(actualRule.getId());
            assertTrue(rules.contains(dbRule));
        }

        Set<Language> actualLanguageSet = new HashSet<>();

        List<Translation> actualTranslations = actualPhrase.getTranslations();
        assertEquals(translations.size(), actualTranslations.size());
        for (Translation actualTranslation: actualTranslations){
            Translation dbTranslation = translationRepository.getOne(actualTranslation.getId());
            assertTrue(translations.contains(dbTranslation));

            actualLanguageSet.add(languageRepository.getOne(actualTranslation.getLanguage().getId()));
        }

        Set<Language> expectedLanguageSet = new HashSet<>();
        expectedLanguageSet.add(testLanguageRU);
        expectedLanguageSet.add(testLanguageBLR);
        assertEquals(expectedLanguageSet,actualLanguageSet);

        // удаляем полную фразу из базы
        phraseRepository.delete(testPhrase);

        // проверяем наличие/отсутствие необходимой информации
        assertEquals(0, phraseRepository.count());
        assertEquals(0, translationRepository.count());
        assertEquals(0, resourceRepository.count());
        assertEquals(2, languageRepository.count());
        assertEquals(2, ruleRepository.count());
        assertEquals(1, wordRepository.count());
        assertEquals(1, contextRepository.count());
    }
}
