package by.zapolski.english.service.lemma;

import by.zapolski.english.lemma.domain.Lemma;
import by.zapolski.english.lemma.dto.LemmaWithSimilarityDto;
import by.zapolski.english.repository.dictionary.LemmaRepository;
import by.zapolski.english.service.lemma.api.LemmaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = LemmaServiceTestConfiguration.class)
public class LemmaServiceTest {

    @Autowired
    private LemmaService lemmaService;

    @MockBean
    private LemmaRepository lemmaRepository;

    @Test
    public void getSimilarWordsWithAccuracyThreshold_test() {
        when(lemmaRepository.findAll()).thenReturn(
                Arrays.asList(
                        new Lemma("table", 1000, 'n'),
                        new Lemma("chair", 1001, 'n')
                )
        );

        List<LemmaWithSimilarityDto> actualLemmas = lemmaService.getSimilarWordsWithAccuracyThreshold("table", 100);
        assertEquals(1, actualLemmas.size());
    }
}
