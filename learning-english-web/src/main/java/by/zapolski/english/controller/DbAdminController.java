package by.zapolski.english.controller;

import by.zapolski.english.service.api.DbLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbAdminController {

    @Autowired
    private DbLoaderService dbLoaderService;

    @GetMapping("/database/reset")
    public String resetDb() {
        dbLoaderService.resetDatabase();
        return "OK";
    }

}
