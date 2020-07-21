package by.zapolski.english.controller.dictionary.web;

import by.zapolski.english.dictionary.dto.DictionaryDto;
import by.zapolski.english.dictionary.dto.DictionaryWithSimilarityDto;
import by.zapolski.english.dictionary.dto.SearchDictionaryRequest;
import by.zapolski.english.service.dictionary.api.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class DictionaryMvcController {

    @Autowired
    DictionaryService dictionaryService;

    @GetMapping("/dictionary/search")
    public String search(Model model) {
        SearchDictionaryRequest searchDictionaryRequest = new SearchDictionaryRequest();
        model.addAttribute("searchRequest", searchDictionaryRequest);
        return "dictionary-search";
    }

    @PostMapping("/dictionary/search")
    public String showSearchResult(
            @ModelAttribute("searchRequest") @Valid SearchDictionaryRequest searchRequest,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "dictionary-search";
        }
        model.addAttribute("searchRequest", searchRequest);
        List<DictionaryWithSimilarityDto> result =
                dictionaryService.getSimilarWordsWithAccuracyThreshold(
                        searchRequest.getWord(),
                        searchRequest.getThreshold());

        model.addAttribute("lemmas", result);
        return "dictionary-list";
    }

    @GetMapping("/dictionary/add")
    public String add(Model model) {
        DictionaryDto dictionaryDto = new DictionaryDto();
        model.addAttribute("lemma", dictionaryDto);
        return "dictionary-add";
    }

    @GetMapping("/dictionary/update")
    public String update(Model model) {
        DictionaryDto dictionaryDto = new DictionaryDto();
        model.addAttribute("lemma", dictionaryDto);
        return "dictionary-update";
    }

}
