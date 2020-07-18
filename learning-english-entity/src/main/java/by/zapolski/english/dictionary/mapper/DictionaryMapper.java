package by.zapolski.english.dictionary.mapper;

import by.zapolski.english.dictionary.domain.Dictionary;
import by.zapolski.english.dictionary.dto.DictionaryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DictionaryMapper {

    DictionaryDto dictionaryToDto(Dictionary dictionary);

    Dictionary dtoToDictionary(DictionaryDto dictionaryDto);

}
