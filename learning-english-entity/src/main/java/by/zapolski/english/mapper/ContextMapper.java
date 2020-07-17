package by.zapolski.english.mapper;

import by.zapolski.english.domain.Context;
import by.zapolski.english.dto.ContextDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContextMapper {
    ContextDto contextToDto(Context source);
}
