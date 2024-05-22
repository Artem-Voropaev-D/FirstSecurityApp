package ru.voropaev.project.SecurityApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.voropaev.project.SecurityApp.security.PersonDetails;
import ru.voropaev.project.SecurityApp.services.AdminService;

@Controller
public class HelloController {
    private final AdminService adminService;

    @Autowired
    public HelloController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    //будем обращаться из браузера для аутентифицированного пользователя -> будут посылаться куки из сессии по этим кукам будет возвращаться объект наш персондетайлс
    @GetMapping("/showUserInfo")
    public String showUserInfo() {
        //таким образом получаем доступ к пользователю из сессии
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails)authentication.getPrincipal(); // получаем данные пользователя
        System.out.println(personDetails.getPerson()); // и после того как получили пользователя выведем на экран
        return "hello"; // так заглушка
    }


    @GetMapping("/admin")
    public String adminPage(){
        adminService.doAdminStuff();
        return "admin";
    }
}
