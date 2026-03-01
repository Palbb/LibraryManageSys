package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.Entities.Reader;
import com.LibraryManagmentSystem.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderService {
    private ReaderRepository readerRepository;
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public Reader getReaderByEmail(String email){
        return readerRepository.findByEmailIs(email);
    }

    public List<Reader> findByFullName(String fullName){
        return readerRepository.findByFullNameContainingIgnoreCase(fullName);
    }
}
