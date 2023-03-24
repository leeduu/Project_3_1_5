package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ModelAndView showAllUsers(Principal principal, Model model) {   // Показ всех юзеров
        model.addAttribute("showAllUsers", userService.showAllUsers());
        model.addAttribute("rolesList", roleService.getRolesList());
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("auth_user", user);
        return new ModelAndView("admin");
    }

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

    @GetMapping("/new") // форма создания нового юзера
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("rolesList", roleService.getRolesList());
        return "new";
    }

    @PostMapping("/new")    // сохранение нового юзера и показ всех юзеров
    public String newUser(@ModelAttribute("user") User user,
                          @RequestParam(name = "role", defaultValue = "1") String role) {
        Set<Role> newRoles = new HashSet<>();
            Long roleId = Long.valueOf(role);
            newRoles.add(roleService.findRole(roleId));
        user.setRoles(newRoles);
        userService.save(user);
        return "redirect:/api/admin";
    }

    @DeleteMapping("/{deleteId}")    //удаление юзера
    public String deleteUser(@PathVariable("deleteId") Long id) {
        userService.delete(id);
        return "redirect:/api/admin";
    }

}
