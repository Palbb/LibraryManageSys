package com.LibraryManagmentSystem.repository;

import com.LibraryManagmentSystem.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorContaining(String author);

    List<Book> findByIsbnContaining(String isbn);

    List<Book> findByAvailableCopiesGreaterThan(int limit);
}
