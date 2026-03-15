package com.LibraryManagmentSystem.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi noAccountApi() {
        return GroupedOpenApi.builder()
                .group("No Account")
                .packagesToScan("com.LibraryManagmentSystem.controller")
                .pathsToMatch("/api/books/search/**", "/api/accounts/registration")
                .build();
    }

    @Bean
    public GroupedOpenApi registeredApi() {
        return GroupedOpenApi.builder()
                .group("Registered")
                .packagesToScan("com.LibraryManagmentSystem.controller")
                .pathsToMatch("/api/books/search/**", "/api/accounts/registration", "/api/borrow/borrowBook", "/api/borrow/return/{id}")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("Admin")
                .packagesToScan("com.LibraryManagmentSystem.controller")
                .build();
    }
}
