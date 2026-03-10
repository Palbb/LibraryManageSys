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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowService {

    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public List<BorrowRecord> getAllBorrow() {
        log.info("Fetching all active borrow records");
        var result =  borrowRecordRepository.findByIsReturnedFalse();
        log.debug("Found {} active borrow records", result.size());
        return result;
    }

    public List<BorrowDebtorResponce> overdueBorrow(LocalDate date) {
        log.info("Searching for overdue borrows as of date: {}", date);
        var list = borrowRecordRepository.findByReturnDeadLineBeforeAndIsReturnedFalse(date);
        log.info("Found {} overdue records to process", list.size());
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
    public BorrowResponce createBorrowBook(
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

        if (!isAdmin && !reader.getFullName().equals(currentUsername)) {
            log.error("User {} tried to borrow book for reader {}", currentUsername, reader.getFullName());
            throw new AccessDeniedException("Вы можете бронировать книги только для себя!");
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
                .orElseThrow(() -> {
                    log.error("Return failed: Record ID {} not found", recordId);
                    return new NoSuchElementException("Record not found :" + recordId);
                });
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

