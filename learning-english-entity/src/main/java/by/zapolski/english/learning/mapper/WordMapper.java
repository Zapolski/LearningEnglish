package by.zapolski.english.learning.mapper;

import by.zapolski.english.learning.domain.Word;
import by.zapolski.english.learning.dto.WordDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WordMapper {
    WordDto wordToDto(Word source);
}
