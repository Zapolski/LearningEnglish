package by.zapolski.english.service.learning.core;

import by.zapolski.english.learning.domain.Word;
import by.zapolski.english.service.CrudBaseServiceImpl;
import by.zapolski.english.service.learning.api.WordService;
import org.springframework.stereotype.Service;

@Service
public class WordServiceImpl extends CrudBaseServiceImpl<Word, Long> implements WordService {
}
