package by.zapolski.english.mapper;

import by.zapolski.english.domain.*;
import by.zapolski.english.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PhraseMapper {

    @Mappings({@Mapping(target = "translations", qualifiedByName = "translationToDto")})
    PhraseDto phraseToDto(Phrase phrase);

    @Mappings({@Mapping(target = "phrase", expression = "java(null)")})
    TranslationDto translationToDto(Translation translation);

}
