package by.zapolski.english.controller.dictionary;

import by.zapolski.english.dictionary.dto.SentenceInfoDto;
import by.zapolski.english.service.dictionary.api.SentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SentenceController {

    @Autowired
    private SentenceService sentenceService;

    @PostMapping("sentences/info")
    public SentenceInfoDto getSentenceInfo(@RequestBody String sentence) {
        return sentenceService.getSentenceInfo(sentence);
    }

}
