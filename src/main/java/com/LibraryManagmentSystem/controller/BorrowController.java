package com.LibraryManagmentSystem.controller;

import com.LibraryManagmentSystem.Entities.BorrowRecord;
import com.LibraryManagmentSystem.dto.BorrowDebtorRequest;
import com.LibraryManagmentSystem.dto.BorrowRequest;
import com.LibraryManagmentSystem.dto.BorrowResponce;
import com.LibraryManagmentSystem.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;
    private LocalDate LocalDate;

    public BorrowController( BorrowService borrowService) {

        this.borrowService = borrowService;
    }
    @PostMapping("/borrowBook")
    public ResponseEntity<BorrowResponce> borrowBook(
            @Valid
            @RequestBody BorrowRequest borrowRequest
            ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(borrowService.createBorrowBook(borrowRequest.getBookId(), borrowRequest.getReaderId()));
    }
    @PutMapping("/return/{id}")
    public ResponseEntity<Void> returnBook(
            @PathVariable Long id
    ){
        borrowService.returnBorrowBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/alldebtors")
    public ResponseEntity<List<BorrowDebtorRequest>> getDebtors (
    ){
        return ResponseEntity.ok().body(borrowService.overdueBorrow(LocalDate.now()));
    }

}
