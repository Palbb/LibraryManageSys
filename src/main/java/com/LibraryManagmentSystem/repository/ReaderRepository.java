package com.LibraryManagmentSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.LibraryManagmentSystem.Entities.Reader;
import java.util.List;

public interface ReaderRepository extends JpaRepository<Reader,Long> {
    Reader findByEmailIs(String email);

    List<Reader> findByFullNameContainingIgnoreCase(String fullName);
}