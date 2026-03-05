package com.LibraryManagmentSystem.dto;

import jakarta.validation.constraints.NotBlank;

public class BookRequest {
    @NotBlank(message = "The title must be written")
    private String title;
    @NotBlank(message = "The book author be written")
    private String author;
    @NotBlank(message = "The book name be written")
    private String name;
    @NotBlank(message = "The book isbn be written")
    private String isbn;

    private int availableCopies;

    public int getAvailableCopies() {
        return availableCopies;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getIsbn() {
        return isbn;
    }
}
