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


}
