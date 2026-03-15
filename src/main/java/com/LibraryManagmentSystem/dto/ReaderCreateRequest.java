package com.LibraryManagmentSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ReaderCreateRequest {
    @NotBlank(message = "The fullname must be written")
    private String fullName;
    @NotBlank(message = "The email must be written correctly")
    @Email()
    private String email;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
