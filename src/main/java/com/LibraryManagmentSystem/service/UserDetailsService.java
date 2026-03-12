package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    AccountRepository accountRepository;

    public UserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user1 = accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with this username does not exists"));
        return User.builder().password(user1.getPassword()).username(user1.getUsername()).authorities(("ROLE_"+user1.getRole()).trim().toUpperCase()).build();
    }
}
