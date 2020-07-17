package by.zapolski.english.mapper;

import by.zapolski.english.domain.Word;
import by.zapolski.english.dto.WordDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WordMapper {
    WordDto wordToDto(Word source);
}
