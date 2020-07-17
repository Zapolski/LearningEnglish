package by.zapolski.english.service.core;

import by.zapolski.english.domain.Word;
import by.zapolski.english.dto.WordDto;
import by.zapolski.english.mapper.WordMapper;
import by.zapolski.english.repository.WordRepository;
import by.zapolski.english.service.api.WordService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WordServiceImpl implements WordService {

    @Autowired
    WordRepository wordRepository;

    @Autowired
    private WordMapper wordMapper = Mappers.getMapper(WordMapper.class);

    @Override
    public List<WordDto> getAll() {

        List<Word> words = wordRepository.findAll();

        return words.stream()
                .map( word -> wordMapper.wordToDto(word))
                .collect(Collectors.toList());
    }
}
