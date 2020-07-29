package by.zapolski.english.controller.dictionary.web;

import by.zapolski.english.dictionary.dto.DictionaryDto;
import by.zapolski.english.dictionary.dto.DictionaryWithSimilarityDto;
import by.zapolski.english.dictionary.dto.SearchDictionaryRequest;
import by.zapolski.english.dictionary.dto.SearchDictionaryRequestByRank;
import by.zapolski.english.service.dictionary.api.DictionaryService;
import by.zapolski.english.service.learning.core.loader.DbLoaderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DictionaryMvcController {

    private static final Logger log = LoggerFactory.getLogger(DbLoaderServiceImpl.class);

    @Autowired
    DictionaryService dictionaryService;

    @GetMapping("/dictionary/search")
    public String search(
            @ModelAttribute("searchRequest") SearchDictionaryRequest searchDictionaryRequest,
            @ModelAttribute("searchDictionaryRequestByRank") SearchDictionaryRequestByRank searchDictionaryRequestByRank,
            @ModelAttribute("lemma") DictionaryDto dictionaryDto,
            Model model) {

        if (searchDictionaryRequest.getWord() != null && !searchDictionaryRequest.getWord().isEmpty()) {
            List<DictionaryWithSimilarityDto> result = dictionaryService.getSimilarWordsWithAccuracyThreshold(
                    searchDictionaryRequest.getWord(),
                    searchDictionaryRequest.getThreshold());
            model.addAttribute("lemmas", result);
        }

        if (searchDictionaryRequestByRank.getSearchRank() != null) {
            List<DictionaryDto> dtoList = dictionaryService.getByRank(searchDictionaryRequestByRank.getSearchRank());
            List<DictionaryWithSimilarityDto> result = dtoList.stream()
                    .map(d -> new DictionaryWithSimilarityDto(d.getId(), d.getValue(), d.getRank(), d.getPartOfSpeech(), 100d))
                    .collect(Collectors.toList());
            model.addAttribute("lemmas", result);
        }

        return "dictionary-search";
    }

    @PostMapping("/dictionary/search/by/similarity")
    public String showSearchResult(
            @ModelAttribute("searchDictionaryRequestByRank") SearchDictionaryRequestByRank searchDictionaryRequestByRank,
            @ModelAttribute("lemma") DictionaryDto dictionaryDto,
            @ModelAttribute("searchRequest") @Valid SearchDictionaryRequest searchRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("searchDictionaryRequestByRank", searchDictionaryRequestByRank);
            model.addAttribute("lemma", dictionaryDto);
            return "dictionary-search";
        }
        redirectAttributes.addFlashAttribute("searchRequest", searchRequest);
        return "redirect:/dictionary/search";
    }

    @PostMapping("/dictionary/search/by/rank")
    public String showSearchResultByRank(
            @ModelAttribute("lemma") DictionaryDto dictionaryDto,
            @ModelAttribute("searchRequest") SearchDictionaryRequest searchRequest,
            @ModelAttribute("searchDictionaryRequestByRank") @Valid SearchDictionaryRequestByRank searchDictionaryRequestByRank,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("searchRequest", searchRequest);
            model.addAttribute("lemma", dictionaryDto);
            return "dictionary-search";
        }
        redirectAttributes.addFlashAttribute("searchDictionaryRequestByRank", searchDictionaryRequestByRank);
        return "redirect:/dictionary/search";
    }

    @PostMapping("/dictionary/add")
    public String addPost(
            @RequestParam(value = "word") String word,
            @RequestParam(value = "threshold") Integer threshold,
            @RequestParam(value = "searchRank") Integer searchRank,
            @ModelAttribute("lemma") @Valid DictionaryDto dictionaryDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("searchRequest", new SearchDictionaryRequest(word, threshold));
            model.addAttribute("searchDictionaryRequestByRank", new SearchDictionaryRequestByRank(searchRank));
            return "dictionary-search";
        }
        redirectAttributes.addFlashAttribute("searchRequest", new SearchDictionaryRequest(word, threshold));
        redirectAttributes.addFlashAttribute("searchDictionaryRequestByRank", new SearchDictionaryRequestByRank(searchRank));
        dictionaryService.save(dictionaryDto);
        return "redirect:/dictionary/search";
    }

    @GetMapping("/dictionary/edit")
    public String edit(
            @RequestParam(value = "word") String word,
            @RequestParam(value = "threshold") Integer threshold,
            @RequestParam(value = "searchRank") Integer searchRank,
            @RequestParam(value = "id") long id,
            Model model
    ) {
        DictionaryDto dictionaryDto = dictionaryService.getById(id);
        model.addAttribute("lemma", dictionaryDto);
        model.addAttribute("word", word);
        model.addAttribute("threshold", threshold);
        model.addAttribute("searchRank", searchRank);
        return "dictionary-update";
    }

    @PostMapping("/dictionary/update")
    public String update(
            @RequestParam(value = "word") String word,
            @RequestParam(value = "threshold") Integer threshold,
            @RequestParam(value = "searchRank") Integer searchRank,
            @ModelAttribute("lemma") @Valid DictionaryDto dictionaryDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("word", word);
            model.addAttribute("threshold", threshold);
            model.addAttribute("searchRank", searchRank);
            return "dictionary-update";
        }

        log.info("{}", dictionaryDto);
        dictionaryService.save(dictionaryDto);
        redirectAttributes.addFlashAttribute("searchRequest", new SearchDictionaryRequest(word, threshold));
        redirectAttributes.addFlashAttribute("searchDictionaryRequestByRank", new SearchDictionaryRequestByRank(searchRank));
        return "redirect:/dictionary/search";
    }

    @GetMapping("/dictionary/delete")
    public String delete(
            @RequestParam(value = "word") String word,
            @RequestParam(value = "threshold") Integer threshold,
            @RequestParam(value = "searchRank") Integer searchRank,
            @RequestParam(value = "id") long id,
            RedirectAttributes redirectAttributes
    ) {
        dictionaryService.deleteById(id);
        redirectAttributes.addFlashAttribute("searchRequest", new SearchDictionaryRequest(word, threshold));
        redirectAttributes.addFlashAttribute("searchDictionaryRequestByRank", new SearchDictionaryRequestByRank(searchRank));
        return "redirect:/dictionary/search";
    }

}
