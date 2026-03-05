package com.LibraryManagmentSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.LibraryManagmentSystem.Entities.Reader;
import java.util.List;
import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader,Long> {
    Optional<Reader> findByEmailIs(String email);

    boolean existsById(Long id);

    boolean existsByEmail(String email);


    Optional<Reader> findByFullNameContainingIgnoreCase(String fullName);
}