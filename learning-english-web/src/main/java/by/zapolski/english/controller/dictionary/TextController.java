package by.zapolski.english.controller.dictionary;

import by.zapolski.english.service.dictionary.api.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping
public class TextController {

    @Autowired
    private TextService textService;

    @PostMapping("text/lemmas")
    public Set<String> getUniqueLemmasFromText(@RequestBody String text) {
        return textService.getUniqueLemmasFromText(text);
    }

}
              