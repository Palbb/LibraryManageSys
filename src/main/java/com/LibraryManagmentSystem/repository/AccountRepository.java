package com.LibraryManagmentSystem.repository;

import com.LibraryManagmentSystem.Entities.Account;
import com.LibraryManagmentSystem.Entities.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account , Long> {
    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

}
