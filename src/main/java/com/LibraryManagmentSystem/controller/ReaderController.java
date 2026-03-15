package com.LibraryManagmentSystem.controller;

import com.LibraryManagmentSystem.dto.ReaderCreateRequest;
import com.LibraryManagmentSystem.dto.ReaderResponse;
import com.LibraryManagmentSystem.service.ReaderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {
    public final ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/email/{email}")
    public ResponseEntity<ReaderResponse> getByEmail(
        @PathVariable String email
    ) {
        return ResponseEntity.ok(readerService.getReaderByEmail(email));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/name/{fullName}")
    public ResponseEntity<ReaderResponse> getbyFullName(
            @PathVariable String fullName
    ){
        return ResponseEntity.ok(readerService.getByFullName(fullName));
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<ReaderResponse> createReader(
            @Valid @RequestBody ReaderCreateRequest dto,
            Authentication authentication
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(readerService.createReader(dto , authentication.getName()));
    }
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReader(
            @PathVariable Long id,
            Authentication authentication
    ){
        readerService.deleteReader(id , authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
