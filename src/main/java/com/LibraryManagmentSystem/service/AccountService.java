package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.Entities.Account;
import com.LibraryManagmentSystem.Entities.Reader;
import com.LibraryManagmentSystem.dto.AccountCreateRequest;
import com.LibraryManagmentSystem.dto.AccountResponse;
import com.LibraryManagmentSystem.repository.AccountRepository;
import com.LibraryManagmentSystem.repository.ReaderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ReaderRepository readerRepository;

    public AccountService(AccountRepository accountRepository, BCryptPasswordEncoder passwordEncoder, ReaderRepository readerRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.readerRepository = readerRepository;
    }

    @Transactional
    public AccountResponse createAccount(AccountCreateRequest dto){
         if (accountRepository.existsByUsername(dto.getUsername())){
            throw new IllegalArgumentException("Username already taken");
        }
         if (readerRepository.existsByEmail(dto.getEmail())){
             throw new IllegalArgumentException("Email already taken");
         }
        Account account = new Account();
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setUsername(dto.getUsername());
        account.setRoles("USER");
        accountRepository.save(account);
        Reader reader = new Reader();
        reader.setEmail(dto.getEmail());
        reader.setFullName(dto.getFullName());
        reader.setAccount(account);
        readerRepository.save(reader);
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setReaderid(reader.getId());
        accountResponse.setId(account.getId());
        accountResponse.setUsername(dto.getUsername());
        accountResponse.setFullname(dto.getFullName());
        accountResponse.setEmail(dto.getEmail());
        return accountResponse;
    }
}
