package by.zapolski.english.controller.lemma.web;

import by.zapolski.english.lemma.dto.LemmaDto;
import by.zapolski.english.lemma.dto.LemmaWithSimilarityDto;
import by.zapolski.english.lemma.dto.SearchLemmaRequest;
import by.zapolski.english.lemma.dto.SearchLemmaRequestByRank;
import by.zapolski.english.service.lemma.api.LemmaService;
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
public class LemmaMvcController {

    private static final Logger log = LoggerFactory.getLogger(DbLoaderServiceImpl.class);

    @Autowired
    LemmaService lemmaService;

    @GetMapping("/lemma/search")
    public String search(
            @ModelAttribute("searchRequest") SearchLemmaRequest searchLemmaRequest,
            @ModelAttribute("searchLemmaRequestByRank") SearchLemmaRequestByRank searchLemmaRequestByRank,
            @ModelAttribute("lemma") LemmaDto lemmaDto,
            Model model) {

        if (searchLemmaRequest.getWord() != null && !searchLemmaRequest.getWord().isEmpty()) {
            List<LemmaWithSimilarityDto> result = lemmaService.getSimilarWordsWithAccuracyThreshold(
                    searchLemmaRequest.getWord(),
                    searchLemmaRequest.getThreshold());
            model.addAttribute("lemmas", result);
        }

        if (searchLemmaRequestByRank.getSearchRank() != null) {
            List<LemmaDto> dtoList = lemmaService.getByRank(searchLemmaRequestByRank.getSearchRank());
            List<LemmaWithSimilarityDto> result = dtoList.stream()
                    .map(d -> new LemmaWithSimilarityDto(d.getId(), d.getValue(), d.getRank(), d.getPartOfSpeech(), 100d))
                    .collect(Collectors.toList());
            model.addAttribute("lemmas", result);
        }

        return "lemma-search";
    }

    @PostMapping("/lemma/search/by/similarity")
    public String showSearchResult(
            @ModelAttribute("searchLemmaRequestByRank") SearchLemmaRequestByRank searchLemmaRequestByRank,
            @ModelAttribute("lemma") LemmaDto lemmaDto,
            @ModelAttribute("searchRequest") @Valid SearchLemmaRequest searchRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("searchLemmaRequestByRank", searchLemmaRequestByRank);
            model.addAttribute("lemma", lemmaDto);
            return "lemma-search";
        }
        redirectAttributes.addFlashAttribute("searchRequest", searchRequest);
        return "redirect:/lemma/search";
    }

    @PostMapping("/lemma/search/by/rank")
    public String showSearchResultByRank(
            @ModelAttribute("lemma") LemmaDto lemmaDto,
            @ModelAttribute("searchRequest") SearchLemmaRequest searchRequest,
            @ModelAttribute("searchLemmaRequestByRank") @Valid SearchLemmaRequestByRank searchLemmaRequestByRank,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("searchRequest", searchRequest);
            model.addAttribute("lemma", lemmaDto);
            return "lemma-search";
        }
        redirectAttributes.addFlashAttribute("searchLemmaRequestByRank", searchLemmaRequestByRank);
        return "redirect:/lemma/search";
    }

    @PostMapping("/lemma/add")
    public String addPost(
            @RequestParam(value = "word") String word,
            @RequestParam(value = "threshold") Integer threshold,
            @RequestParam(value = "searchRank") Integer searchRank,
            @ModelAttribute("lemma") @Valid LemmaDto lemmaDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("searchRequest", new SearchLemmaRequest(word, threshold));
            model.addAttribute("searchLemmaRequestByRank", new SearchLemmaRequestByRank(searchRank));
            return "lemma-search";
        }
        redirectAttributes.addFlashAttribute("searchRequest", new SearchLemmaRequest(word, threshold));
        redirectAttributes.addFlashAttribute("searchLemmaRequestByRank", new SearchLemmaRequestByRank(searchRank));
        lemmaService.save(lemmaDto);
        return "redirect:/lemma/search";
    }

    @GetMapping("/lemma/edit")
    public String edit(
            @RequestParam(value = "word") String word,
            @RequestParam(value = "threshold") Integer threshold,
            @RequestParam(value = "searchRank") Integer searchRank,
            @RequestParam(value = "id") long id,
            Model model
    ) {
        LemmaDto lemmaDto = lemmaService.getById(id);
        model.addAttribute("lemma", lemmaDto);
        model.addAttribute("word", word);
        model.addAttribute("threshold", threshold);
        model.addAttribute("searchRank", searchRank);
        return "lemma-update";
    }

    @PostMapping("/lemma/update")
    public String update(
            @RequestParam(value = "word") String word,
            @RequestParam(value = "threshold") Integer threshold,
            @RequestParam(value = "searchRank") Integer searchRank,
            @ModelAttribute("lemma") @Valid LemmaDto lemmaDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("word", word);
            model.addAttribute("threshold", threshold);
            model.addAttribute("searchRank", searchRank);
            return "lemma-update";
        }

        log.info("{}", lemmaDto);
        lemmaService.save(lemmaDto);
        redirectAttributes.addFlashAttribute("searchRequest", new SearchLemmaRequest(word, threshold));
        redirectAttributes.addFlashAttribute("searchLemmaRequestByRank", new SearchLemmaRequestByRank(searchRank));
        return "redirect:/lemma/search";
    }

    @GetMapping("/lemma/delete")
    public String delete(
            @RequestParam(value = "word") String word,
            @RequestParam(value = "threshold") Integer threshold,
            @RequestParam(value = "searchRank") Integer searchRank,
            @RequestParam(value = "id") long id,
            RedirectAttributes redirectAttributes
    ) {
        lemmaService.deleteById(id);
        redirectAttributes.addFlashAttribute("searchRequest", new SearchLemmaRequest(word, threshold));
        redirectAttributes.addFlashAttribute("searchLemmaRequestByRank", new SearchLemmaRequestByRank(searchRank));
        return "redirect:/lemma/search";
    }

}
