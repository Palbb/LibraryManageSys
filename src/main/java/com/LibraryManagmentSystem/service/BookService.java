package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.Entities.Book;
import com.LibraryManagmentSystem.dto.BookRequest;
import com.LibraryManagmentSystem.dto.BookResponse;
import com.LibraryManagmentSystem.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service

public class BookService {
    private final BookRepository bookRepository;
    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> getAllBooks(){
        log.info("Fetching all books from database");
        var save = toBookResponces(bookRepository.findAll());
        log.debug("Found {} books", save.size());
        return save;
    }

    public BookResponse getBookById(Long id){

        log.info("Finding book by ID: {}", id);
         var bookbyid =  bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book search failed: ID {} not found", id);
                    return new NoSuchElementException("Book with id " + id + " does not exists");
                });

        var save = toBookResponce(bookbyid);
        log.debug("Found {} book", save.getId());
        return save;
    }

    public List<BookResponse> getBooksByAuthor(String author) {
        log.info("Searching books by author: {}", author);
        return toBookResponces(bookRepository.findByAuthorContaining(author));
    }

    public BookResponse createBook(BookRequest dto){
        log.info("Attempting to create a new book with ISBN: {}", dto.getIsbn());
        Book book = new Book();
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setName(dto.getName());
        book.setTitle(dto.getTitle());
        book.setAvailableCopies(dto.getAvailableCopies());
        if (bookRepository.existsByIsbn(book.getIsbn())){
            log.warn("Book creation failed: ISBN {} already exists", dto.getIsbn());
            throw new IllegalStateException("A book with this ISBN already exists : " + book.getIsbn());
        }
        var save = bookRepository.save(book);
        log.info("Book successfully created with ID: {} and Name: {}", save.getId(), save.getName());
        return toBookResponce(save);
    }

    public void deleteBook(String name){
        log.info("Attempting to delete book by name: {}", name);
        if (!bookRepository.existsByName(name)){
            log.warn("Delete failed: No book found with name containing '{}'", name);
            throw new IllegalStateException("A book with this name does not exists : " + name);
        }
        Book delete = bookRepository.findByNameContaining(name).get(0);
        bookRepository.delete(delete);
        log.info("Book '{}' (ID: {}) successfully deleted", delete.getName(), delete.getId());
    }

    private List<BookResponse> toBookResponces(List<Book> list){
        List<BookResponse> bookResponses = new ArrayList<>();
        return list.stream()
                .map(this::toBookResponce)
                .toList();
    }
    private BookResponse toBookResponce(Book book){

        BookResponse dto = new BookResponse();

            dto.setId(book.getId());
            dto.setAuthor(book.getAuthor());
            dto.setTitle(book.getTitle());
            dto.setName(book.getName());
            dto.setIsbn(book.getIsbn());
            dto.setAvailableCopies((book.getAvailableCopies()));

        return dto;
    }
}
