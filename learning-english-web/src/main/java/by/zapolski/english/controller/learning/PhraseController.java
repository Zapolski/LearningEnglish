package by.zapolski.english.controller.learning;

import by.zapolski.english.learning.dto.PhraseDto;
import by.zapolski.english.service.learning.api.PhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PhraseController {

    @Autowired
    private PhraseService phraseService;

    @GetMapping("/phrases/{word}")
    public List<PhraseDto> getPhrasesForWord(
            @PathVariable String word,
            @RequestParam(
                    defaultValue = "0",
                    required = false) Integer minRank,
            @RequestParam(
                    defaultValue = "2147483647",
                    required = false) Integer maxRank
    ) {
        return phraseService.getPhrasesByWord(word, minRank, maxRank);
    }

//    @GetMapping("/records/query/{query}/{param}")
//    public List<Phrase> getAllPhrasesQueryString(
//            @PathVariable String query,
//            @PathVariable int param,
//            @RequestParam Integer minRank,
//            @RequestParam Integer maxRank
//    ) {
//        return recordDao.getPhrasesByEnglishValueWithSqlLike(query, param, minRank, maxRank);
//    }
//
//    @PutMapping("/records/{id}")
//    public ResponseEntity<Phrase> updatePhrase(@RequestBody Phrase record, @PathVariable int id) {
//        Phrase recordsById = recordDao.getPhrasesById(id);
//        if (recordsById.getId() == 0) {
//            return ResponseEntity.notFound().build();
//        }
//        try {
//            recordDao.update(record);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("records")
//    public ResponseEntity<Set<Phrase>> getPhrasesByIds(@RequestParam int[] ids) {
//        return new ResponseEntity<>(recordService.getPhrasesByIds(ids), HttpStatus.OK);
//    }
//
//    @GetMapping("records/rank")
//    public ResponseEntity<List<Phrase>> getPhrasesWithRank(
//            @RequestParam Integer minRank,
//            @RequestParam Integer maxRank
//    ) {
//        return new ResponseEntity<>(recordService.getAllPhrasesWithRank(minRank, maxRank), HttpStatus.OK);
//    }


}
