package com.LibraryManagmentSystem.dto;


import java.time.LocalDate;

public class BorrowResponse {
    private Long id;
    private String nameBook;
    private LocalDate returnDeadLine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameBook() {
        return nameBook;
    }

    public void setNameBook(String nameBook) {
        this.nameBook = nameBook;
    }

    public LocalDate getReturnDeadLine() {
        return returnDeadLine;
    }

    public void setReturnDeadLine(LocalDate returnDeadLine) {
        this.returnDeadLine = returnDeadLine;
    }
}