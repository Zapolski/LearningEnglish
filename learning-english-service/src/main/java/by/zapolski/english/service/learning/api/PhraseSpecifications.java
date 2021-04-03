package by.zapolski.english.service.learning.api;

import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.dto.PhraseSearchDto;
import org.springframework.data.jpa.domain.Specification;

public interface PhraseSpecifications {

    Specification<Phrase> getSpecification(PhraseSearchDto search);
}
