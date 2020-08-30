package by.zapolski.english.controller;

import by.zapolski.english.security.dto.UserRegistrationDto;
import by.zapolski.english.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String registration(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());

        return "registration";
    }

    @PostMapping
    public String registerUser(
            @ModelAttribute("userRegistrationDto") @Valid UserRegistrationDto userRegistrationDto,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userRegistrationDto.getPassword().equals(userRegistrationDto.getPasswordConfirmation())) {
            model.addAttribute("passwordError", true);
            return "registration";
        }
        if (!userService.registerUser(userRegistrationDto)) {
            model.addAttribute("userNameError", true);
            return "registration";
        }

        return "redirect:/";
    }
}