package com.LibraryManagmentSystem.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;


public class AccountCreateRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String fullName;
    @Email
    private String email;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
}
