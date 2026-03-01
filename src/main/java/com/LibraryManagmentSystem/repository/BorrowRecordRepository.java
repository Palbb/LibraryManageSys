package com.LibraryManagmentSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.LibraryManagmentSystem.Entities.BorrowRecord;

import java.time.LocalDate;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    //Сколько выдано книг и где они находятся
    List<BorrowRecord> findByIsReturnedFalse();
    //Поиск просрочки
    List<BorrowRecord> findByReturnDeadLineBeforeAndIsReturnedFalse(LocalDate date);
    //Какие книги у читателя
    List<BorrowRecord> findByReaderId(Long readerId);
    //Вся история книги
    List<BorrowRecord> findByBookId(Long bookId);
    //Текущая активная выдача книги
    List<BorrowRecord> findByBookIdAndIsReturnedFalse(Long bookId);
}