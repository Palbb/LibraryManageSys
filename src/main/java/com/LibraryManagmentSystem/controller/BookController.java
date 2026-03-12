package com.LibraryManagmentSystem.controller;

import com.LibraryManagmentSystem.dto.BookRequest;
import com.LibraryManagmentSystem.dto.BookResponse;
import com.LibraryManagmentSystem.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/search/allBooks")
    public ResponseEntity<List<BookResponse>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable Long id
    ){
            return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> getByAuthor(
            @RequestParam(required = false) String author
    ){
        if (author== null || author.isBlank() )
            return ResponseEntity.ok(bookService.getAllBooks());
        else
            return ResponseEntity.ok(bookService.getBooksByAuthor(author));
        }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<BookResponse> createBook(
            @Valid
            @RequestBody BookRequest dto
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(dto));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id
    ){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(bookService.deleteBook(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/delivery/{copies}/")
    public ResponseEntity<BookResponse> deliveryBook(
            @PathVariable Integer copies ,
            @RequestParam String name
    ){
        return ResponseEntity.ok(bookService.addCopies(copies, name));
    }
}