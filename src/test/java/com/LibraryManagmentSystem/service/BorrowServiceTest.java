//package com.LibraryManagmentSystem.service;
//
//import com.LibraryManagmentSystem.Entities.Book;
//import com.LibraryManagmentSystem.Entities.BorrowRecord;
//import com.LibraryManagmentSystem.Entities.Reader;
//import com.LibraryManagmentSystem.repository.BookRepository;
//import com.LibraryManagmentSystem.repository.BorrowRecordRepository;
//import com.LibraryManagmentSystem.repository.ReaderRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class BorrowServiceTest {
//
//    Book book1 = new Book();
//    Reader reader1 = new Reader();
//    BorrowRecord borrowRecord = new BorrowRecord();
//
//    @BeforeEach
//    void setUp(){
//        book1.setId(1L);
//        book1.setName("State");
//        book1.setTitle("Philosophy");
//        book1.setAvailableCopies(10);
//        book1.setAuthor("Plato");
//        book1.setIsbn("9785389062566");
//        reader1.setId(1L);
//        reader1.setEmail("stuffyGuy@example.com");
//        reader1.setFullName("Philosophy Fan");
//        reader1.setRegistrationDate(LocalDate.now());
//        borrowRecord.setBorrowTime(LocalDate.now().minusWeeks(2));
//        borrowRecord.setReturnDeadLine(LocalDate.now());
//        borrowRecord.setBook(book1);
//        borrowRecord.setReader(reader1);
//        borrowRecord.setId(1L);
//        borrowRecord.setReturned(false);
//    }
//
//    @Mock
//    BorrowRecordRepository borrowRecordRepository;
//    @Mock
//    BookRepository bookRepository;
//    @Mock
//    ReaderRepository readerRepository;
//
//    @InjectMocks
//    BorrowService borrowService;
//    @InjectMocks
//    BookService bookService;
//    @InjectMocks
//    ReaderService readerService;
//
//    @Test
//    void getAllBorrow() {
//        List<BorrowRecord> borrowRecordList = new ArrayList<>();
//        borrowRecordList.add(borrowRecord);
//
//        when(borrowRecordRepository.findByIsReturnedFalse()).thenReturn(borrowRecordList);
//
//        var result = borrowService.getAllBorrow();
//
//        assertNotNull(result);
//        assertEquals(1,result.size());
//
//        verify(borrowRecordRepository, times(1)).findByIsReturnedFalse();
//
//    }
//
//    @Test
//    void booksFromReader() {
//        List<BorrowRecord> borrowRecordList = new ArrayList<>();
//        borrowRecordList.add(borrowRecord);
//
//        when(borrowRecordRepository.findByReaderId(reader1.getId())).thenReturn(borrowRecordList);
//
//        var result = borrowService.booksFromReader(reader1.getId());
//
//        assertNotNull(result);
//        assertEquals(1,result.size());
//
//        verify(borrowRecordRepository, times(1)).findByReaderId(reader1.getId());
//
//    }
//
//    @Test
//    void bookHistory() {
//        List<BorrowRecord> borrowRecordList = new ArrayList<>();
//        borrowRecordList.add(borrowRecord);
//
//        when(borrowRecordRepository.findByBookId(book1.getId())).thenReturn(borrowRecordList);
//
//        var result = borrowService.bookHistory(book1.getId());
//
//        assertNotNull(result);
//        assertEquals(1,result.size());
//
//        verify(borrowRecordRepository, times(1)).findByBookId(book1.getId());
//
//
//    }
//
//    @Test
//    void activeBorrow() {
//        List<BorrowRecord> borrowRecordList = new ArrayList<>();
//        borrowRecordList.add(borrowRecord);
//
//        when(borrowRecordRepository.findByBookIdAndIsReturnedFalse(book1.getId())).thenReturn(borrowRecordList);
//
//        var result = borrowService.activeBorrow(borrowRecord.getId());
//
//        assertNotNull(result);
//        assertEquals(1,result.size());
//
//        verify(borrowRecordRepository, times(1)).findByBookIdAndIsReturnedFalse(borrowRecord.getId());
//
//    }
//
//    @Test
//    void overdueBorrowSuccess() {
//        borrowRecord.setReturnDeadLine(LocalDate.now().minusDays(1));
//        List<BorrowRecord> borrowRecordList = new ArrayList<>();
//        borrowRecordList.add(borrowRecord);
//
//        when(borrowRecordRepository.findByReturnDeadLineBeforeAndIsReturnedFalse(LocalDate.now())).thenReturn(borrowRecordList);
//
//        var result =  borrowService.overdueBorrow(LocalDate.now());
//
//        assertNotNull(result);
//        assertEquals(1,result.size());
//
//        verify(borrowRecordRepository, times(1)).findByReturnDeadLineBeforeAndIsReturnedFalse(LocalDate.now());
//
//    }
//
//    @Test
//    void overdueBorrowEmpty() {
//        when(borrowRecordRepository.findByReturnDeadLineBeforeAndIsReturnedFalse(LocalDate.now())).thenReturn(List.of());
//
//        var result =  borrowService.overdueBorrow(LocalDate.now());
//
//        assertNotNull(result);
//        assertEquals(0,result.size());
//
//        verify(borrowRecordRepository, times(1)).findByReturnDeadLineBeforeAndIsReturnedFalse(LocalDate.now());
//
//    }
//
//    @Test
//    void createBorrowBookSuccess() {
//
//        when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
//        when(readerRepository.findById(reader1.getId())).thenReturn(Optional.of(reader1));
//        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(borrowRecord);
//
//        var result = borrowService.createBorrowBook(book1.getId(), reader1.getId());
//
//        assertNotNull(result);
//        assertEquals(9 , book1.getAvailableCopies() );
//        assertEquals("State" , result.getNameBook());
//
//        verify(bookRepository, times(1)).save(book1);
//        verify(borrowRecordRepository, times(1)).save(any(BorrowRecord.class));
//
//    }
//    @Test
//    void createBorrowBookFailure() {
//        book1.setAvailableCopies(0);
//        when(bookRepository.findById(book1.getId())).thenReturn(Optional.of(book1));
//        when(readerRepository.findById(reader1.getId())).thenReturn(Optional.of(reader1));
//
//        assertThrows(IllegalStateException.class, () -> {
//            borrowService.createBorrowBook(book1.getId(), reader1.getId());
//        });
//
//        verify(borrowRecordRepository, never()).save(any());
//    }
//
//    @Test
//    void createBorrowBookFailure_BookNotFound() {
//
//        when(bookRepository.findById(book1.getId())).thenReturn(Optional.empty());
//
//        assertThrows(NoSuchElementException.class, () -> {
//            borrowService.createBorrowBook(1L, 1L);
//        });
//
//        verify(borrowRecordRepository, never()).save(any());
//    }
//
//    @Test
//    void createBorrowBookFailure_ReaderNotFound() {
//
//        when(readerRepository.findById(reader1.getId())).thenReturn(Optional.empty());
//
//        assertThrows(NoSuchElementException.class, () -> {
//            borrowService.createBorrowBook(1L, 1L);
//        });
//
//        verify(borrowRecordRepository, never()).save(any());
//    }
//
//    @Test
//    void returnBorrowBookSuccess() {
//        when(borrowRecordRepository.findById(borrowRecord.getId())).thenReturn(Optional.of(borrowRecord));
//
//        borrowService.returnBorrowBook(borrowRecord.getId());
//
//        assertTrue(borrowRecord.isReturned());
//        assertEquals(11 , book1.getAvailableCopies());
//
//        verify(borrowRecordRepository, times(1)).save(borrowRecord);
//        verify(bookRepository, times(1)).save(any(Book.class));
//
//    }
//    @Test
//    void returnBorrowBookFailure() {
//        when(borrowRecordRepository.findById(borrowRecord.getId())).thenReturn(Optional.empty());
//
//        assertThrows(NoSuchElementException.class, () -> {
//            borrowService.returnBorrowBook(1L);
//        });
//
//        verify(borrowRecordRepository, never()).save(any());
//        verify(bookRepository, never()).save(any(Book.class));
//    }
//}