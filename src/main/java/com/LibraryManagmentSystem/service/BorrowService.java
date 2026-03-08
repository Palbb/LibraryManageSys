package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.Entities.Book;
import com.LibraryManagmentSystem.Entities.BorrowRecord;
import com.LibraryManagmentSystem.Entities.Reader;
import com.LibraryManagmentSystem.dto.BorrowDebtorResponce;
import com.LibraryManagmentSystem.dto.BorrowResponce;
import com.LibraryManagmentSystem.repository.BookRepository;
import com.LibraryManagmentSystem.repository.BorrowRecordRepository;
import com.LibraryManagmentSystem.repository.ReaderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    public List<BorrowDebtorResponce> overdueBorrow(LocalDate date) {
        var list = borrowRecordRepository.findByReturnDeadLineBeforeAndIsReturnedFalse(date);
        List<BorrowDebtorResponce> borrowDebtorResponces = new ArrayList<>();
        for (BorrowRecord borrowRecord : list){
            BorrowDebtorResponce borrowDebtorResponce = new BorrowDebtorResponce();
            borrowDebtorResponce.setBookName(borrowRecord.getBook().getName());
            borrowDebtorResponce.setReaderName(borrowRecord.getReader().getFullName());
            borrowDebtorResponce.setDueDate(borrowRecord.getReturnDeadLine());
            borrowDebtorResponce.setDaysOverdue(ChronoUnit.DAYS.between(borrowRecord.getReturnDeadLine() , date));
            borrowDebtorResponces.add(borrowDebtorResponce);
        }
        return borrowDebtorResponces;
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
    @Transactional
    public BorrowResponce createBorrowBook(
            Long id,
            Long readerId
    ) {
        Book book = bookRepository.findById(id).
                orElseThrow(() -> new NoSuchElementException("Book not found : " + id));
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new NoSuchElementException("Reader not found : " + readerId));
        if (book.getAvailableCopies()<=0){
            throw new IllegalStateException("Not found avaible copies : " + book.getAvailableCopies());
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(book);
        borrowRecord.setReader(reader);
        borrowRecord.setReturnDeadLine(LocalDate.now().plusDays(14));
        BorrowRecord save = borrowRecordRepository.save(borrowRecord);
        BorrowResponce borrowResponce = new BorrowResponce();
        borrowResponce.setId(save.getId());
        borrowResponce.setNameBook(save.getBook().getName());
        borrowResponce.setReturnDeadLine(save.getReturnDeadLine());
        return borrowResponce;

    }
    @Transactional
    public void returnBorrowBook(
            Long recordId
    ){
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new NoSuchElementException("Record not found :" + recordId));
        if (record.isReturned()) {
            throw new IllegalArgumentException("Book was already returned : " + record.isReturned());
        }
        record.setReturned(true);
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        borrowRecordRepository.save(record);
        bookRepository.save(book);
    }

}

