package com.LibraryManagmentSystem.controller;

import com.LibraryManagmentSystem.dto.AccountAdminResponce;
import com.LibraryManagmentSystem.dto.AccountCreateRequest;
import com.LibraryManagmentSystem.dto.AccountResponse;
import com.LibraryManagmentSystem.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/registration")
    public ResponseEntity<AccountResponse> registration(
            @Valid
            @RequestBody AccountCreateRequest dto
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/promote")
    public ResponseEntity<AccountAdminResponce> promote(
            @PathVariable  Long id
    ){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountService.promotion(id));
    }

}
