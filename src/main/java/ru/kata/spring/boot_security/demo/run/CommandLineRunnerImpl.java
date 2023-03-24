package ru.kata.spring.boot_security.demo.run;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    public CommandLineRunnerImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {

        Set<Role> userRoles = new HashSet<>();
        Role userRole = new Role(1L, "ROLE_USER");
        roleService.saveRole(userRole);
        userRoles.add(userRole);

        Set<Role> adminRoles = new HashSet<>();
        Role adminRole = new Role(2L, "ROLE_ADMIN");
        roleService.saveRole(adminRole);
        adminRoles.add(adminRole);
        adminRoles.add(userRole);

        User testUser = new User("test1", "$2a$12$1gGEhpUmRrlFSagYpU4Vt.CrRu9eNe.2gZsyaU6vAlpNIYwUN17PW", "test1@mail.ru", userRoles);
        User testAdmin = new User("tester", "$2a$12$QaFtIAbJZaz4oM1jao02ze2HEqSbejlj2sr7DcUHczm0S8j9x/Uj6", "tester@bk.ru", adminRoles);
        try {
            userService.update(testUser);
        } catch (Exception e) {
        }
        try {
            userService.update(testAdmin);
        } catch (Exception e) {
        }
    }
}
