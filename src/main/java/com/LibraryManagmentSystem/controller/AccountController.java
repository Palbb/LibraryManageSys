package com.LibraryManagmentSystem.controller;

import com.LibraryManagmentSystem.dto.AccountCreateRequest;
import com.LibraryManagmentSystem.dto.AccountResponse;
import com.LibraryManagmentSystem.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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


}
