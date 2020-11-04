package by.zapolski.english.service.lemma;

import by.zapolski.english.repository.dictionary.LemmaRepository;
import by.zapolski.english.service.lemma.api.LemmaService;
import by.zapolski.english.service.lemma.core.LemmaServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
public class LemmaServiceTestConfiguration {

    @Bean
    public LemmaService lemmaService(LemmaRepository lemmaRepository) {
        return new LemmaServiceImpl(lemmaRepository);
    }
}
