package com.LibraryManagmentSystem.dto;

import com.LibraryManagmentSystem.Entities.Book;
import com.LibraryManagmentSystem.Entities.Reader;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class BorrowDebtorRequest {
    @NotNull
    private String readerName;
    @NotNull
    private String bookName;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private Long daysOverdue;

    public void setDaysOverdue(Long daysOverdue) {
        this.daysOverdue = daysOverdue;
    }

    public Long getDaysOverdue() {
        return daysOverdue;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }


    public String getReaderName() {
        return readerName;
    }

    public String getBookName() {
        return bookName;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

}
