package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.Entities.Book;
import com.LibraryManagmentSystem.Entities.BorrowRecord;
import com.LibraryManagmentSystem.Entities.Reader;
import com.LibraryManagmentSystem.dto.BorrowDebtorResponse;
import com.LibraryManagmentSystem.dto.BorrowResponse;
import com.LibraryManagmentSystem.repository.BookRepository;
import com.LibraryManagmentSystem.repository.BorrowRecordRepository;
import com.LibraryManagmentSystem.repository.ReaderRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
@Service

public class BorrowService {

    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    public BorrowService(ReaderRepository readerRepository, BookRepository bookRepository, BorrowRecordRepository borrowRecordRepository) {
        this.readerRepository = readerRepository;
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public List<BorrowRecord> getAllBorrow() {
        log.info("Fetching all active borrow records");
        var result =  borrowRecordRepository.findByIsReturnedFalse();
        log.debug("Found {} active borrow records", result.size());
        return result;
    }

    public List<BorrowDebtorResponse> overdueBorrow(LocalDate date) {
        log.info("Searching for overdue borrows as of date: {}", date);
        var list = borrowRecordRepository.findByReturnDeadLineBeforeAndIsReturnedFalse(date);
        log.info("Found {} overdue records to process", list.size());
        List<BorrowDebtorResponse> borrowDebtorResponces = new ArrayList<>();
        for (BorrowRecord borrowRecord : list){
            BorrowDebtorResponse borrowDebtorResponce = new BorrowDebtorResponse();
            borrowDebtorResponce.setBookName(borrowRecord.getBook().getName());
            borrowDebtorResponce.setReaderName(borrowRecord.getReader().getFullName());
            borrowDebtorResponce.setDueDate(borrowRecord.getReturnDeadLine());
            borrowDebtorResponce.setDaysOverdue(ChronoUnit.DAYS.between(borrowRecord.getReturnDeadLine() , date));
            borrowDebtorResponces.add(borrowDebtorResponce);
        }
        return borrowDebtorResponces;
    }

    public List<BorrowRecord> booksFromReader(Long id) {
        log.info("Fetching borrow history for reader ID: {}", id);
        return borrowRecordRepository.findByReaderId(id);
    }

    public List<BorrowRecord> bookHistory(Long id) {
        log.info("Fetching full history for book ID: {}", id);
        return borrowRecordRepository.findByBookId(id);
    }

    public List<BorrowRecord> activeBorrow(Long id) {
        log.info("Checking active borrows for book ID: {}", id);
        return borrowRecordRepository.findByBookIdAndIsReturnedFalse(id);
    }
    @Transactional
    public BorrowResponse createBorrowBook(
            Long id,
            Long readerId,
            String currentUsername
    ) {
        log.info("Request to borrow book ID: {} by reader ID: {}", id, readerId);
        Book book = bookRepository.findById(id).
                orElseThrow(() -> {
                    log.error("Create borrow failed: Book not found with ID: {}", id);
                    return new NoSuchElementException("Book not found : " + id);
                });
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> {
                    log.error("Create borrow failed: Reader not found with ID: {}", readerId);
                    return new NoSuchElementException("Reader not found : " + readerId);
                });
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !reader.getAccount().getUsername().equals(currentUsername)) {
            log.error("User {} tried to borrow book for reader {}", currentUsername, reader.getFullName());
            throw new AccessDeniedException("User can borrow books only for your account");
        }
        if (book.getAvailableCopies()<=0){
            log.warn("Create borrow rejected: Book '{}' (ID: {}) has no available copies", book.getName(), id);
            throw new IllegalStateException("Not found avaible copies : " + book.getAvailableCopies());
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(book);
        borrowRecord.setReader(reader);
        borrowRecord.setReturnDeadLine(LocalDate.now().plusDays(14));
        BorrowRecord save = borrowRecordRepository.save(borrowRecord);
        log.info("Book '{}' successfully borrowed by '{}'. Return deadline: {}",
                book.getName(), reader.getFullName(), save.getReturnDeadLine());
        BorrowResponse borrowResponse = new BorrowResponse();
        borrowResponse.setId(save.getId());
        borrowResponse.setNameBook(save.getBook().getName());
        borrowResponse.setReturnDeadLine(save.getReturnDeadLine());
        return borrowResponse;

    }
    @Transactional
    public void returnBorrowBook(
            Long recordId,
            String username
    ){
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> {
                    log.error("Return failed: Record ID {} not found", recordId);
                    return new NoSuchElementException("Record not found :" + recordId);
                });
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !record.getReader().getAccount().getUsername().equals(username)){
            throw new AccessDeniedException("User trying to delete someone else's borrow");
        }
        if (record.isReturned()) {
            log.warn("Return rejected: Record ID {} already marked as returned", recordId);
            throw new IllegalArgumentException("Book was already returned : " + record.isReturned());
        }
        record.setReturned(true);
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        borrowRecordRepository.save(record);
        bookRepository.save(book);
        log.info("Book '{}' successfully returned. Stock updated to: {}", book.getName(), book.getAvailableCopies());
    }

}

