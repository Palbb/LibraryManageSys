package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.Entities.Book;
import com.LibraryManagmentSystem.dto.BookRequest;
import com.LibraryManagmentSystem.dto.BookResponce;
import com.LibraryManagmentSystem.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository repository) {
        this.bookRepository = repository;
    }


    public List<BookResponce> getAllBooks(){
        return toBookResponces(bookRepository.findAll());
    }

    public BookResponce getBookById(Long id){

         var bookbyid =  bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book with id "+ id + " does not exists"));

        BookResponce dto = new BookResponce();

        dto.setAuthor(bookbyid.getAuthor());
        dto.setIsbn(bookbyid.getIsbn());
        dto.setName(bookbyid.getName());
        dto.setTitle(bookbyid.getTitle());

        return dto;
    }

    public List<BookResponce> getBooksByAuthor(String author) {

        return toBookResponces(bookRepository.findByAuthorContaining(author));
    }

    public BookResponce createBook(BookRequest dto){
        Book book = new Book();
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setName(dto.getName());
        book.setTitle(dto.getTitle());
        book.setAvailableCopies(dto.getAvailableCopies());
        if (bookRepository.existsByIsbn(book.getIsbn())){
            throw new IllegalStateException("A book with this ISBN already exists : " + book.getIsbn());
        }
        var save = bookRepository.save(book);
        return toBookResponce(save);
    }

    public void deleteBook(String name){
        if (!bookRepository.existsByName(name)){
            throw new IllegalStateException("A book with this name does not exists : " + name);
        }
        Book delete = bookRepository.findByNameContaining(name).get(0);
        bookRepository.delete(delete);
    }

    private List<BookResponce> toBookResponces(List<Book> list){
        List<BookResponce> bookResponces = new ArrayList<>();

        for (Book book : list){
            BookResponce dto = new BookResponce();
            dto.setId(book.getId());
            dto.setAuthor(book.getAuthor());
            dto.setTitle(book.getTitle());
            dto.setName(book.getName());
            dto.setIsbn(book.getIsbn());
            dto.setAvailableCopies(book.getAvailableCopies());
            bookResponces.add(dto);
        }
        return bookResponces;
    }
    private BookResponce toBookResponce(Book book){

        BookResponce dto = new BookResponce();

            dto.setId(book.getId());
            dto.setAuthor(book.getAuthor());
            dto.setTitle(book.getTitle());
            dto.setName(book.getName());
            dto.setIsbn(book.getIsbn());
            dto.setAvailableCopies((book.getAvailableCopies()));

        return dto;
    }
}
