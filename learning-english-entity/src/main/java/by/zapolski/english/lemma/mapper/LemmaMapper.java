package by.zapolski.english.lemma.mapper;

import by.zapolski.english.lemma.domain.Lemma;
import by.zapolski.english.lemma.dto.LemmaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LemmaMapper {

    LemmaDto lemmaToDto(Lemma lemma);

    Lemma dtoToLemma(LemmaDto lemmaDto);

}
