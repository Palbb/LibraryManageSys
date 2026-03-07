package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.Entities.Book;
import com.LibraryManagmentSystem.dto.BookRequest;
import com.LibraryManagmentSystem.dto.BookResponce;
import com.LibraryManagmentSystem.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private Book book1 = new Book();
    private Book book2 = new Book();
    @BeforeEach
    void setup() {
        book1.setId(1L);
        book1.setTitle("Education");
        book1.setAuthor("Me");
        book1.setName("Java");
        book1.setIsbn("12315532");
        book1.setAvailableCopies(10);
        book2.setId(2L);
        book2.setTitle("Else");
        book2.setAuthor("Not me");
        book2.setName("Not Java");
        book2.setIsbn("12315533");
        book2.setAvailableCopies(20);

    }

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    @Test
    void getAllBooks() {
        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(book1);
        mockBooks.add(book2);
        when(bookRepository.findAll()).thenReturn(mockBooks);
        List<BookResponce> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(2 , result.size());
        assertEquals("Else", result.get(1).getTitle());

        verify(bookRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getBookByIdSuccess() {
        when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
        BookResponce responce = bookService.getBookById(book1.getId());

        assertNotNull(responce, "Responce shouldn't be empty");
        assertEquals(book1.getTitle(), responce.getTitle(), " Title must be equal");

        verify(bookRepository, Mockito.times(1)).findById(book1.getId());
    }
    @Test
    void getBookByIdFailure() {
        when(bookRepository.findById(book1.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            bookService.getBookById(book1.getId());
        });
        verify(bookRepository, Mockito.times(1)).findById(book1.getId());
    }

    @Test
    void getBooksByAuthorSuccess() {
        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(book1);
        when(bookRepository.findByAuthorContaining(book1.getAuthor())).thenReturn(mockBooks);
        List<BookResponce> result = bookService.getBooksByAuthor(book1.getAuthor());

        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals("Me", result.get(0).getAuthor());

        verify(bookRepository, Mockito.times(1)).findByAuthorContaining(book1.getAuthor());
    }
    @Test
    void getBooksByAuthorFailure() {
        when(bookRepository.findByAuthorContaining(book1.getAuthor())).thenReturn(List.of());

        var result = bookService.getBooksByAuthor(book1.getAuthor());

        assertNotNull(result);
        assertEquals(0,result.size());

        verify(bookRepository, Mockito.times(1)).findByAuthorContaining(book1.getAuthor());
    }

    @Test
    void createBookSuccess() {
        BookRequest request = new BookRequest();
        request.setAuthor(book1.getAuthor());
        request.setIsbn(book1.getIsbn());
        request.setName(book1.getName());
        request.setTitle(book1.getTitle());
        request.setAvailableCopies(book1.getAvailableCopies());
        when(bookRepository.existsByIsbn(request.getIsbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        BookResponce result = bookService.createBook(request);

        assertNotNull(result);
        assertEquals(book1.getTitle(), result.getTitle());

        verify(bookRepository, Mockito.times(1)).existsByIsbn(request.getIsbn());
        verify(bookRepository, Mockito.times(1)).save(any(Book.class));
    }

    @Test
    void createBookFailure(){
        BookRequest request = new BookRequest();
        request.setAuthor(book1.getAuthor());
        request.setIsbn(book1.getIsbn());
        request.setName(book1.getName());
        request.setTitle(book1.getTitle());
        request.setAvailableCopies(book1.getAvailableCopies());

        when(bookRepository.existsByIsbn(request.getIsbn())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> {
            bookService.createBook(request);
        });

        verify(bookRepository, never()).save(any(Book.class));

    }

    @Test
    void deleteBookSuccess(){
        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(book1);
        when(bookRepository.existsByName(book1.getName())).thenReturn(true);
        when(bookRepository.findByNameContaining(book1.getName())).thenReturn(mockBooks);

        bookService.deleteBook(mockBooks.get(0).getName());

        verify(bookRepository, Mockito.times(1)).delete(mockBooks.get(0));
    }
    @Test
    void deleteBookfailure(){
        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(book1);
        when(bookRepository.existsByName(book1.getName())).thenReturn(false);

        assertThrows(IllegalStateException.class , ()->{
            bookService.deleteBook(mockBooks.get(0).getName());
        });
        verify(bookRepository, never()).findByNameContaining(anyString());
        verify(bookRepository, never()).delete(any());
    }
}