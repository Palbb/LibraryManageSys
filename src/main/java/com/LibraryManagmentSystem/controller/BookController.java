package com.LibraryManagmentSystem.controller;

import com.LibraryManagmentSystem.dto.BookRequest;
import com.LibraryManagmentSystem.dto.BookResponce;
import com.LibraryManagmentSystem.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<BookResponce>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<BookResponce> getBookById(
            @PathVariable Long id
    ){
            return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponce>> getByAuthor(
            @RequestParam(required = false) String author
    ){
        if (author== null || author.isBlank() )
            return ResponseEntity.ok(bookService.getAllBooks());
        else
            return ResponseEntity.ok(bookService.getBooksByAuthor(author));
        }

    @PostMapping
    public ResponseEntity<BookResponce> createBook(
            @Valid
            @RequestBody BookRequest dto
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(dto));
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable String name
    ){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}