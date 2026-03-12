package com.LibraryManagmentSystem.dto;

import com.LibraryManagmentSystem.Entities.Role;


public class AccountAdminResponce {
    private String username;
    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
