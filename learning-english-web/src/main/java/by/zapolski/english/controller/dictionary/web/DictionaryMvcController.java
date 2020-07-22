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

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Log4j2
public class DictionaryMvcController {

    @Autowired
    DictionaryService dictionaryService;

    @GetMapping("/dictionary/search")
    public String search(
            @ModelAttribute("searchRequest") SearchDictionaryRequest searchDictionaryRequest,
            @ModelAttribute("lemmas") ArrayList<DictionaryWithSimilarityDto> lemmas,
            Model model) {
        return "dictionary-search";
    }

    @PostMapping("/dictionary/search")
    public String showSearchResult(
            @ModelAttribute("lemmas") ArrayList<DictionaryWithSimilarityDto> lemmas,
            @ModelAttribute("searchRequest") @Valid SearchDictionaryRequest searchRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "dictionary-search";
        }

        List<DictionaryWithSimilarityDto> result =
                dictionaryService.getSimilarWordsWithAccuracyThreshold(
                        searchRequest.getWord(),
                        searchRequest.getThreshold());

        model.addAttribute("lemmas", result);
        return "dictionary-search";
    }

    @PostMapping("/dictionary/search/rank")
    public String showSearchResultByRank(
            @ModelAttribute("rank") String rank,
            @ModelAttribute("searchRequest") SearchDictionaryRequest searchRequest,
            Model model
    ) {
        List<DictionaryDto> dtoList = dictionaryService.getByRank(Integer.valueOf(rank));
        List<DictionaryWithSimilarityDto> result = dtoList.stream()
                .map(d -> new DictionaryWithSimilarityDto(d.getId(),d.getValue(),d.getRank(),d.getPartOfSpeech(),100d))
                .collect(Collectors.toList());


        model.addAttribute("lemmas", result);
        return "dictionary-search";
    }

    @GetMapping("/dictionary/add")
    public String add(
            @ModelAttribute("lemma") DictionaryDto dictionaryDto,
            Model model
    ) {
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

        List<DictionaryWithSimilarityDto> result =
                dictionaryService.getSimilarWordsWithAccuracyThreshold(dictionaryDto.getValue(), 100);
        SearchDictionaryRequest searchRequest = new SearchDictionaryRequest(dictionaryDto.getValue(), 100);

        model.addAttribute("lemmas", result);
        model.addAttribute("searchRequest", searchRequest);
        return "dictionary-search";
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
            return "dictionary-update";
        }

        dictionaryDto.setId(id);
        DictionaryDto newDictionaryDto = dictionaryService.save(dictionaryDto);

        List<DictionaryWithSimilarityDto> result =
                dictionaryService.getSimilarWordsWithAccuracyThreshold(newDictionaryDto.getValue(), 100);
        SearchDictionaryRequest searchRequest = new SearchDictionaryRequest(newDictionaryDto.getValue(), 100);

        model.addAttribute("lemmas", result);
        model.addAttribute("searchRequest", searchRequest);

        return "dictionary-search";
    }

    //
    @GetMapping("/dictionary/delete/{id}")
    public String delete(
            @PathVariable("id") long id,
            Model model
    ) {

        DictionaryDto dictionaryDto = dictionaryService.getById(id);
        dictionaryService.deleteById(id);

        List<DictionaryWithSimilarityDto> result =
                dictionaryService.getSimilarWordsWithAccuracyThreshold(dictionaryDto.getValue(), 100);
        SearchDictionaryRequest searchRequest = new SearchDictionaryRequest(dictionaryDto.getValue(), 100);

        model.addAttribute("lemmas", result);
        model.addAttribute("searchRequest", searchRequest);

        return "dictionary-search";
    }

}
