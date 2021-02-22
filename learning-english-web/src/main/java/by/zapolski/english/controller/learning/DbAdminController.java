package by.zapolski.english.controller.learning;

import by.zapolski.english.service.learning.api.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbAdminController {

    @Autowired
    private DbService dbService;

    @GetMapping("/database/reset")
    public String resetDb() {
        dbService.resetDatabase();
        return "OK";
    }

    @GetMapping("/database/rank/correct")
    public String correctRanks() {
        dbService.correctRanks();
        return "OK";
    }

    @GetMapping("/database/phrase/rank/correct")
    public String correctPhrasesRanks(){
        dbService.correctPhrasesRanks();
        return "OK";
    }

    @GetMapping("/database/backup")
    public String backup(){
        dbService.backup();
        return "OK";
    }

}
