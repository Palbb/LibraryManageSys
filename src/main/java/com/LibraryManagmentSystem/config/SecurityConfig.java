package com.LibraryManagmentSystem.config;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/accounts/registration").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                .requestMatchers("/api/borrow/borrowBook").hasRole("USER")
                .requestMatchers("/api/borrow/return/{id}").hasRole("USER")
                .anyRequest().authenticated());
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(EntryPoint()).accessDeniedHandler(DeniedHandler()));
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
    @Bean
    public BCryptPasswordEncoder Encoder(){
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B);
    }

    @Bean
    public AuthenticationEntryPoint EntryPoint(){
        return ((request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            String jsonResponce = """
                    {
                    "error": "unauthorized",
                    "message": "enter your login and password"
                    }
                    """;
            response.getWriter().write(jsonResponce);
            response.getWriter().flush();
        });
    }

    @Bean
    public AccessDeniedHandler DeniedHandler(){
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            String jsonResponce = """
                    {
                    "error": "access denied",
                    "message": "only for admins"
                    }
                    """;
            response.getWriter().write(jsonResponce);
            response.getWriter().flush();
        };
    }
}
