package by.zapolski.english.repository;

import by.zapolski.english.domain.Rule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RuleRepositoryIntegrationTest {

    @Autowired
    private RuleRepository ruleRepository;

    private Rule testRule;

    @Before
    public void before() {
        testRule = new Rule();
        testRule.setValue("Condition One");
    }

    @Test
    public void createAndReturnById() {
        ruleRepository.save(testRule);
        Rule actualRule = ruleRepository.getOne(testRule.getId());
        assertEquals(testRule, actualRule);
    }

    @Test
    public void createAndReturnByValue() {
        ruleRepository.save(testRule);
        Rule actualRule = ruleRepository.getByValue(testRule.getValue());
        assertEquals(testRule, actualRule);
    }
}
