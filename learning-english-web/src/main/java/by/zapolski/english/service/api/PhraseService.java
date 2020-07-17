package by.zapolski.english.service.api;

import by.zapolski.english.dto.PhraseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PhraseService {

    List<PhraseDto> getPhrasesByWord(String word, Integer minRank, Integer maxRank);
}
