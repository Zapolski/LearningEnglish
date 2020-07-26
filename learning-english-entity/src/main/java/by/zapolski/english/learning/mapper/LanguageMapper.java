package by.zapolski.english.learning.mapper;

import by.zapolski.english.learning.domain.Language;
import by.zapolski.english.learning.dto.LanguageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LanguageMapper {
    LanguageDto languageToDto(Language source);
}
