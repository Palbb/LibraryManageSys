package com.LibraryManagmentSystem.controller;

import com.LibraryManagmentSystem.dto.BorrowDebtorResponse;
import com.LibraryManagmentSystem.dto.BorrowRequest;
import com.LibraryManagmentSystem.dto.BorrowResponse;
import com.LibraryManagmentSystem.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;
    private LocalDate LocalDate;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/borrowBook")
    public ResponseEntity<BorrowResponse> borrowBook(
            @Valid @RequestBody BorrowRequest borrowRequest,
            Authentication authentication
            ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(borrowService.createBorrowBook(borrowRequest.getBookId(), borrowRequest.getReaderId() , authentication.getName()));
    }
    @PutMapping("/return/{id}")
    public ResponseEntity<Void> returnBook(
            @PathVariable Long id ,
            Authentication authentication
    ){
        borrowService.returnBorrowBook(id , authentication.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/alldebtors")
    public ResponseEntity<List<BorrowDebtorResponse>> getDebtors (
    ){
        return ResponseEntity.ok().body(borrowService.overdueBorrow(LocalDate.now()));
    }

}
