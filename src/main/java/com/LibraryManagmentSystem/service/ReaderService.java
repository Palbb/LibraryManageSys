package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.Entities.Reader;
import com.LibraryManagmentSystem.dto.ReaderCreateRequest;
import com.LibraryManagmentSystem.dto.ReaderResponce;
import com.LibraryManagmentSystem.repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class ReaderService {
    private ReaderRepository readerRepository;

    public ReaderResponce getReaderByEmail(String email){
        log.info("Searching for reader by email: {}", email);
        return toReaderResponce(readerRepository.findByEmailIs(email).
                orElseThrow(() -> {
                    log.warn("Reader lookup failed: No record found for email {}", email);
                    return new NoSuchElementException("Reader with this email" + email + " does not exist ");
                }));
    }

    public ReaderResponce getByFullName(String fullName){
        log.info("Searching for reader by name pattern: '{}'", fullName);
        return toReaderResponce( readerRepository.findByFullNameContainingIgnoreCase(fullName)
                .orElseThrow(() -> {
                    log.warn("Reader lookup failed: No name matching '{}'", fullName);
                    return new NoSuchElementException("Reader with this fullname" + fullName + " does not exist ");
                }));

    }

    public ReaderResponce createReader(ReaderCreateRequest dto){
        if (readerRepository.existsByEmail(dto.getEmail())){
            log.warn("Registration rejected: Email {} is already in use", dto.getEmail());
            throw new IllegalArgumentException("Reader with this email already exists");
        }
        Reader reader = new Reader();
        reader.setEmail(dto.getEmail());
        reader.setFullName(dto.getFullName());
        var save = readerRepository.save(reader);
        log.info("Reader successfully registered with ID: {}", save.getId());
        return toReaderResponce(save);
    }

    public void deleteReader(Long id) {
        log.info("Request to delete reader ID: {}", id);
        if (!readerRepository.existsById(id)){
            log.error("Delete failed: Reader with ID {} does not exist", id);
            throw new IllegalArgumentException("Reader with id " + id + " not found");
        }
        readerRepository.deleteById(id);
        log.info("Reader ID: {} successfully removed from system", id);
    }
    public ReaderResponce toReaderResponce(Reader reader){
        ReaderResponce dto = new ReaderResponce();
        dto.setEmail(reader.getEmail());
        dto.setFullName(reader.getFullName());
        dto.setId(reader.getId());
        dto.setRegistrationDate(reader.getRegistrationDate());
        return dto;
    }
}
