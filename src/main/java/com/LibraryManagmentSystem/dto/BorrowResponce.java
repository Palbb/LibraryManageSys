package com.LibraryManagmentSystem.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class BorrowResponce {
    private Long id;
    private String nameBook;
    private LocalDate returnDeadLine;

    public void setId(Long id) {
        this.id = id;
    }

    public void setNameBook(String nameBook) {
        this.nameBook = nameBook;
    }

    public void setReturnDeadLine(LocalDate returnDeadLine) {
        this.returnDeadLine = returnDeadLine;
    }

    public Long getId() {
        return id;
    }

    public String getNameBook() {
        return nameBook;
    }

    public LocalDate getReturnDeadLine() {
        return returnDeadLine;
    }
}