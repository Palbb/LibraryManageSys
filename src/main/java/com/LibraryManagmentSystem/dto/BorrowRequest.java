package com.LibraryManagmentSystem.dto;

import jakarta.validation.constraints.NotNull;

public class BorrowRequest {
        @NotNull(message = "The book must be written")
        private Long bookId;

        @NotNull(message = "The reader must be written")
        private Long readerId;

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getReaderId() {
        return readerId;
    }

}

