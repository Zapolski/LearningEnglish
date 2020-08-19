package by.zapolski.english.service.learning.core;

import by.zapolski.english.learning.domain.Word;
import by.zapolski.english.learning.mapper.WordMapper;
import by.zapolski.english.repository.learning.WordRepository;
import by.zapolski.english.service.learning.api.WordService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;

    public WordServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public List<Word> getAll() {
        return wordRepository.findAll();
    }
}
