package by.zapolski.english.service.api;

import by.zapolski.english.dto.WordDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WordService {
    List<WordDto> getAll();
}
