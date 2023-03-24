package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController                              // добавлено "Rest" // = @Controller для класса + @ResponseBody для каждого метода в классе
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public User getTestUser() {
        return userService.findUser(2L);
    }

//    @GetMapping()
//    public ResponseEntity<Void> getTestUser() {
//        userService.findUser(2L);
//        return ResponseEntity.ok().build();
//    }




// -----------  Методы из 3.1.4 -----------------

//    @GetMapping("/")
//    public String redirectToLoginPage() {
//        return "redirect:/login";
//    }
//
//    @GetMapping("/login")
//    public String loginPage() {
//        return "login_page";
//    }
//
//    @GetMapping("/user")
//    public String showUserProfile(Model model, Principal principal) {
//        User user = userService.findUserByEmail(principal.getName());
//        model.addAttribute("showMyInfo", user);
//        return "user";
//    }



}
