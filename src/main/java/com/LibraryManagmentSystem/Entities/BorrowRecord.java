package com.LibraryManagmentSystem.Entities;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;

    private LocalDate borrowTime = LocalDate.now();
    private LocalDate returnDeadLine;
    private boolean isReturned = false;

    public void setId(Long id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public void setBorrowTime(LocalDate borrowTime) {
        this.borrowTime = borrowTime;
    }

    public void setReturnDeadLine(LocalDate returnDeadLine) {
        this.returnDeadLine = returnDeadLine;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Reader getReader() {
        return reader;
    }

    public LocalDate getBorrowTime() {
        return borrowTime;
    }

    public LocalDate getReturnDeadLine() {
        return returnDeadLine;
    }

    public boolean isReturned() {
        return isReturned;
    }
}
