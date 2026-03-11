package com.LibraryManagmentSystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


public class BorrowRequest {
        @NotNull(message = "The book must be written")
        private Long bookId;

        @NotNull(message = "The reader must be written")
        private Long readerId;

        public Long getBookId() {
                return bookId;
        }

        public void setBookId(Long bookId) {
                this.bookId = bookId;
        }

        public Long getReaderId() {
                return readerId;
        }

        public void setReaderId(Long readerId) {
                this.readerId = readerId;
        }
}

