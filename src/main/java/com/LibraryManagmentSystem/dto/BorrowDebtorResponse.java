package com.LibraryManagmentSystem.dto;

import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;

public class BorrowDebtorResponse {
    @NotNull
    private String readerName;
    @NotNull
    private String bookName;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private Long daysOverdue;


    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getDaysOverdue() {
        return daysOverdue;
    }

    public void setDaysOverdue(Long daysOverdue) {
        this.daysOverdue = daysOverdue;
    }
}
