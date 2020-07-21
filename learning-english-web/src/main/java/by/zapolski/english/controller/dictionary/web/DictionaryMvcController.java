package by.zapolski.english.controller.dictionary.web;

import by.zapolski.english.dictionary.dto.DictionaryDto;
import by.zapolski.english.dictionary.dto.DictionaryWithSimilarityDto;
import by.zapolski.english.dictionary.dto.SearchDictionaryRequest;
import by.zapolski.english.service.dictionary.api.DictionaryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@Log4j2
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

    @PostMapping("/dictionary/add")
    public String addPost(
            @ModelAttribute("lemma") @Valid DictionaryDto dictionaryDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "dictionary-add";
        }
        dictionaryService.save(dictionaryDto);
        model.addAttribute("lemma", new DictionaryDto());
        model.addAttribute("messageResult", "Object was added.");
        return "dictionary-add";
    }

    @GetMapping("/dictionary/edit/{id}")
    public String edit(
            @PathVariable("id") Long id,
            Model model
    ) {
        DictionaryDto dictionaryDto = dictionaryService.getById(id);
        model.addAttribute("lemma", dictionaryDto);
        return "dictionary-update";
    }

    @PostMapping("/dictionary/update/{id}")
    public String update(
            @PathVariable("id") Long id,
            @ModelAttribute("lemma") @Valid DictionaryDto dictionaryDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("lemma",dictionaryDto);
            return "dictionary-update";
        }
        dictionaryDto.setId(id);
        DictionaryDto newDictionaryDto =  dictionaryService.save(dictionaryDto);
        log.info("Out: ",dictionaryDto.toString());
        model.addAttribute("lemma", newDictionaryDto);
        return "dictionary-update";
    }

    @GetMapping("/dictionary/delete/{id}")
    public String delete(
            @PathVariable("id") long id,
            Model model
    ) {
        dictionaryService.deleteById(id);
        return "redirect:/dictionary/search";
    }

}
