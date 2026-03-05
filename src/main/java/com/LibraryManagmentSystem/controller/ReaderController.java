package com.LibraryManagmentSystem.controller;

import com.LibraryManagmentSystem.Entities.Reader;
import com.LibraryManagmentSystem.dto.ReaderCreateRequest;
import com.LibraryManagmentSystem.dto.ReaderResponce;
import com.LibraryManagmentSystem.service.ReaderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {
    public final ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }
    @GetMapping("/search/email/{email}")

    public ResponseEntity<ReaderResponce> getByEmail(
        @PathVariable String email
    ) {
        return ResponseEntity.ok(readerService.getReaderByEmail(email));
    }
    @GetMapping("/search/name/{fullName}")
    public ResponseEntity<ReaderResponce> getbyFullName(
            @PathVariable String fullName
    ){
        return ResponseEntity.ok(readerService.getByFullName(fullName));
    }
    @PostMapping
    public ResponseEntity<ReaderResponce> createReader(
            @Valid
            @RequestBody ReaderCreateRequest dto
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(readerService.createReader(dto));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReader(
            @PathVariable Long id
    ){
        readerService.deleteReader(id);
        return ResponseEntity.noContent().build();
    }
}
