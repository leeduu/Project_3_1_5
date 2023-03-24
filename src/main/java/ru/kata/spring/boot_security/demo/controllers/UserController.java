package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@RestController                              // добавлено "Rest" // = @Controller для класса + @ResponseBody для каждого метода в классе
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public UserDTO getTestUser() {
        return convertToUserDTO(userService.findUser(2L));        // добавить principal ?
    }

    private UserDTO convertToUserDTO(User user) {              // добавлено
        return modelMapper.map(user, UserDTO.class);
    }





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
