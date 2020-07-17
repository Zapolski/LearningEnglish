package by.zapolski.english.repository;

import by.zapolski.english.domain.Context;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ContextRepositoryIntegrationTest {

    @Autowired
    private ContextRepository contextRepository;

    private Context context;

    @Before
    public void before() {
        context = new Context("Some context of using word.");
    }

    @Test
    public void createAndReturnByValue() {
        contextRepository.save(context);
        Context found = contextRepository.getByValue(context.getValue());
        assertThat(found.getValue()).isEqualTo(context.getValue());
    }

    @Test
    public void createAndReturnById() {
        contextRepository.save(context);
        Context found = contextRepository.getOne(context.getId());
        assertThat(found.getValue()).isEqualTo(context.getValue());
    }
}
