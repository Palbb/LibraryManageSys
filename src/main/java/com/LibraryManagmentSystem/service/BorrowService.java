package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.Entities.Book;
import com.LibraryManagmentSystem.Entities.BorrowRecord;
import com.LibraryManagmentSystem.Entities.Reader;
import com.LibraryManagmentSystem.repository.BookRepository;
import com.LibraryManagmentSystem.repository.BorrowRecordRepository;
import com.LibraryManagmentSystem.repository.ReaderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BorrowService {

    private ReaderRepository readerRepository;
    private BookRepository bookRepository;
    private BorrowRecordRepository borrowRecordRepository;

    public BorrowService(ReaderRepository readerRepository, BookRepository bookRepository, BorrowRecordRepository borrowRecordRepository) {
        this.readerRepository = readerRepository;
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public List<BorrowRecord> getAllBorrow() {
        return borrowRecordRepository.findByIsReturnedFalse();
    }

    public List<BorrowRecord> overdueBorrow(LocalDate date) {
        return borrowRecordRepository.findByReturnDeadLineBeforeAndIsReturnedFalse(date);
    }

    public List<BorrowRecord> booksFromReader(Long Id) {
        return borrowRecordRepository.findByReaderId(Id);
    }

    public List<BorrowRecord> bookHistory(Long id) {
        return borrowRecordRepository.findByBookId(id);
    }

    public List<BorrowRecord> activeBorrow(Long id) {
        return borrowRecordRepository.findByBookIdAndIsReturnedFalse(id);
    }

    public BorrowRecord borrowBook(
            Long id,
            Long readerId
    ) {
        Book book = bookRepository.findById(id).
                orElseThrow(() -> new NoSuchElementException("Book not found"));
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new NoSuchElementException("Reader not found"));
        if (book.getAvailableCopies()<=0){
            throw new IllegalStateException("Not found avaible copies");
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(book);
        borrowRecord.setReader(reader);
        borrowRecord.setReturnDeadLine(LocalDate.now().plusDays(14));

        return borrowRecordRepository.save(borrowRecord);

    }
    @Transactional
    public void borrowBack(
            Long recordId
    ){
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new NoSuchElementException("Record bot found"));
        if (record.isReturned()) {
            throw new RuntimeException("Book was returned");
        }
        record.setReturned(true);
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        borrowRecordRepository.save(record);
        bookRepository.save(book);
    }

}

