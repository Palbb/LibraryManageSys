package com.LibraryManagmentSystem.config;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(EntryPoint()).accessDeniedHandler(DeniedHandler()));
        http.exceptionHandling(access -> DeniedHandler());
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                .requestMatchers("/api/borrow/borrowBook","/api/borrow/return/{id}","/api/readers/create").hasRole("USER")
                .requestMatchers("/api/books/**", "/api/borrow/**", "/api/readers").hasRole("ADMIN"));
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
    @Bean
    public UserDetailsService UserDetailsService() throws Exception{
        UserDetails user = User.withUsername("User1").password("{noop}123").roles("USER").build();
        UserDetails admin = User.withUsername("Admin").password("{noop}admin").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(admin, user);
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
