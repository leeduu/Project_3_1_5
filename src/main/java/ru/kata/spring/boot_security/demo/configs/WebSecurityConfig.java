package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.services.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;
    private final UserService userService;
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserService userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "/index", "/login").permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
            .anyRequest().authenticated()
            .and()
            .formLogin().usernameParameter("email")
            .successHandler(successUserHandler)
            .permitAll()
            .and()
            .logout().logoutSuccessUrl("/login")
            .permitAll();
    }






//                .formLogin()
//                .loginPage("/login")
//                .successHandler(successUserHandler)
////                .loginProcessingUrl("/login_process")
//                .usernameParameter("email")
//                .passwordParameter("password")
//                .permitAll();
//        http.logout()
//                .permitAll()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/")
//                .and().csrf().disable();
//        http.authorizeRequests()
//                .antMatchers("/", "/?error").permitAll()
//                .antMatchers("/admin/**").permitAll()//.hasRole("ADMIN")
//                .antMatchers("/user/**").permitAll()//.hasAnyRole("USER", "ADMIN")
//                .anyRequest().authenticated()
//                .and()
//                .csrf().disable();
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {  // существует ли юзер...
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        authenticationProvider.setUserDetailsService(customUserDetailsService);  // ...если да - положить в Spring Security Context
//        return authenticationProvider;
//    }
}