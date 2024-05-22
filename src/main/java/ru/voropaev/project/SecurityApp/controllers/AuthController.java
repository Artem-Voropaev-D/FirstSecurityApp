package ru.voropaev.project.SecurityApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.voropaev.project.SecurityApp.models.Person;
import ru.voropaev.project.SecurityApp.services.RegistrationService;
import ru.voropaev.project.SecurityApp.util.PersonValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final PersonValidator personValidator;

    @Autowired
    public AuthController(RegistrationService registrationService, PersonValidator personValidator) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
    }


    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registerPage(@ModelAttribute("person") Person person ){
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult){
        personValidator.validate(person, bindingResult);//проверили есть ли уже человек с таким именем

        if(bindingResult.hasErrors()){
            return "auth/registration";
        }

        registrationService.register(person);
        return "redirect:/auth/login";//после регистрации человек должен аутентифицироваться
    }
}
