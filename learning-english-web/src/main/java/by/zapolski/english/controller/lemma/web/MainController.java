package by.zapolski.english.controller.lemma.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/training")
    public String main() {
        return "training";
    }
}
