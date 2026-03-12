package com.LibraryManagmentSystem.dto;

import com.LibraryManagmentSystem.Entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


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
