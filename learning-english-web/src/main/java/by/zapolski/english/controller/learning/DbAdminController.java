package by.zapolski.english.controller.learning;

import by.zapolski.english.service.learning.api.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DbAdminController {

    private final DbService dbService;

    @GetMapping("/database/reset")
    public String resetDb() {
        dbService.resetDatabase();
        return "OK";
    }

    @GetMapping(value = "/database/backup")
    public ResponseEntity<byte[]> backup() {
        byte[] resource = dbService.backup();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename("database-backup.xls")
                        .build());
        return ResponseEntity.ok()
                .contentLength(resource.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(httpHeaders)
                .body(resource);
    }

}
