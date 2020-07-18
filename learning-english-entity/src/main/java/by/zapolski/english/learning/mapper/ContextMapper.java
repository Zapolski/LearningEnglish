package by.zapolski.english.learning.mapper;

import by.zapolski.english.learning.domain.Context;
import by.zapolski.english.learning.dto.ContextDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContextMapper {
    ContextDto contextToDto(Context source);
}
