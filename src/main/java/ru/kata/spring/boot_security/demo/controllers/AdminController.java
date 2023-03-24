package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;              // добавлено
    public AdminController(UserService userService,
                           RoleService roleService,
                           ModelMapper modelMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()                                // добавить principal
    public List<UserDTO> showAllUsers() {
        return userService.showAllUsers().stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

//    @GetMapping
//    public ModelAndView showAllUsers(Principal principal, Model model) {   // Показ всех юзеров
//        model.addAttribute("showAllUsers", userService.showAllUsers());
//        model.addAttribute("rolesList", roleService.getRolesList());
//        User user = userService.findUserByEmail(principal.getName());
//        model.addAttribute("auth_user", user);
//        return new ModelAndView("admin");
//    }

    @GetMapping("/{editId}") //форма апдейта юзера
    public String updateUser(Model model, @PathVariable("editId") Long id) {
        model.addAttribute("user", userService.findUser(id));
        model.addAttribute("rolesList", roleService.getRolesList());
        return "update";
    }

    @PatchMapping("/{editId}") // апдейт юзера и показ всех юзеров                      || Прописать принятие 2 ролей
    public String update(@RequestParam(name = "rolesList", defaultValue = "1") String role,
                         @RequestParam(name = "username") String username,
                         @RequestParam(name = "password") String password,
                         @RequestParam(name = "email") String email,
                         @PathVariable("editId") String id) {
        Set<Role> newRoles = new HashSet<>();
        newRoles.add(roleService.findRole(Long.valueOf(role)));
        User user = userService.findUser(Long.valueOf(id));
        user.setUsername(username);
        user.setEmail(email);
        user.setRoles(newRoles);
        if (!password.equals(user.getPassword())) {
            user.setPassword(password);
        }
        user.setPassword(password);
        userService.update(user);
        return "redirect:/api/admin";
    }

    @PostMapping
    public ResponseEntity<HttpStatus> newUser(@RequestBody @Valid UserDTO userDTO,        // в Postman не записываются роли в users_roles
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMSG = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMSG.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            // можно добавить исключение
            // throw new PersonNotCreatedException(errorMSG.toString());
        }
        userService.save(convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);    //HTTP-ответ с пустым телом и статусом ОК
    }

//    @GetMapping("/new") // форма создания нового юзера
//    public String newUserForm(Model model) {
//        model.addAttribute("user", new User());
//        model.addAttribute("rolesList", roleService.getRolesList());
//        return "new";
//    }
//
//    @PostMapping("/new")    // сохранение нового юзера и показ всех юзеров
//    public String newUser(@ModelAttribute("user") User user,
//                          @RequestParam(name = "role", defaultValue = "1") String role) {
//        Set<Role> newRoles = new HashSet<>();
//            Long roleId = Long.valueOf(role);
//            newRoles.add(roleService.findRole(roleId));
//        user.setRoles(newRoles);
//        userService.save(user);
//        return "redirect:/api/admin";
//    }

    @DeleteMapping("/{deleteId}")    //удаление юзера
    public String deleteUser(@PathVariable("deleteId") Long id) {
        userService.delete(id);
        return "redirect:/api/admin";
    }

    private User convertToUser(UserDTO userDTO) {              // добавлено
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {              // добавлено
        return modelMapper.map(user, UserDTO.class);
    }
}
