package com.LibraryManagmentSystem.dto;

import jakarta.validation.constraints.NotBlank;

public class BookRequest {
    @NotBlank(message = "The title must be written")
    private String title;
    @NotBlank(message = "The book author must be written")
    private String author;
    @NotBlank(message = "The book name must be written")
    private String name;
    @NotBlank(message = "The book isbn must be written")
    private String isbn;

    private int availableCopies;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
}
