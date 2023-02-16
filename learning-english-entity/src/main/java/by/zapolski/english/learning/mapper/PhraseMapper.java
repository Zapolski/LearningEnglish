package by.zapolski.english.learning.mapper;

import by.zapolski.english.learning.domain.Phrase;
import by.zapolski.english.learning.domain.Translation;
import by.zapolski.english.learning.dto.PhraseDto;
import by.zapolski.english.learning.dto.TranslationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PhraseMapper {

    @Mappings({@Mapping(target = "translations", qualifiedByName = "translationToDto")})
    PhraseDto phraseToDto(Phrase phrase);

    @Named("translationToDto")
    @Mappings({@Mapping(target = "phrase", expression = "java(null)")})
    TranslationDto translationToDto(Translation translation);

}
