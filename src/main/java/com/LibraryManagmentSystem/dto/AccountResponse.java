package com.LibraryManagmentSystem.dto;



public class AccountResponse {

    private Long readerid;

    private Long id;

    private String username;

    private String email;

    private String fullname;


    public Long getReaderid() {
        return readerid;
    }

    public void setReaderid(Long readerid) {
        this.readerid = readerid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
