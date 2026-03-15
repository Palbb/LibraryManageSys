package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.Entities.Reader;
import com.LibraryManagmentSystem.dto.ReaderCreateRequest;
import com.LibraryManagmentSystem.dto.ReaderResponse;
import com.LibraryManagmentSystem.repository.ReaderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ReaderService {
    private final ReaderRepository readerRepository;
    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public ReaderResponse getReaderByEmail(String email){
        log.info("Searching for reader by email: {}", email);
        return toReaderResponce(readerRepository.findByEmailIs(email).
                orElseThrow(() -> {
                    log.warn("Reader lookup failed: No record found for email {}", email);
                    return new NoSuchElementException("Reader with this email" + email + " does not exist ");
                }));
    }

    public ReaderResponse getByFullName(String fullName){
        log.info("Searching for reader by name pattern: '{}'", fullName);
        return toReaderResponce( readerRepository.findByFullNameContainingIgnoreCase(fullName)
                .orElseThrow(() -> {
                    log.warn("Reader lookup failed: No name matching '{}'", fullName);
                    return new NoSuchElementException("Reader with this fullname" + fullName + " does not exist ");
                }));

    }

    public ReaderResponse createReader(ReaderCreateRequest dto, String username){
        if (readerRepository.existsByEmail(dto.getEmail())){
            log.warn("Registration rejected: Email {} is already in use", dto.getEmail());
            throw new IllegalArgumentException("Reader with this email already exists");
        }
        if (readerRepository.existsByFullName(username)) {
            log.warn("User {} tried to create a second reader profile", username);
            throw new IllegalStateException("Reader profile already exists");
        }
        Reader reader = new Reader();
        reader.setEmail(dto.getEmail());
        reader.setFullName(dto.getFullName());
        var save = readerRepository.save(reader);
        log.info("Reader successfully registered with ID: {}", save.getId());
        return toReaderResponce(save);
    }

    public void deleteReader(Long id , String username) {
        var reader = readerRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("Reader with id " + id + " not found"));
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        log.info("Request to delete reader ID: {}", id);
        if (!reader.getFullName().equals(username)&& !isAdmin){
            log.error("Access denied: User {} tried to delete another reader", username);
            throw new AccessDeniedException("You can't  delete someone else's reader");
        }

        readerRepository.deleteById(id);
        log.info("Reader ID: {} successfully removed from system", id);
    }
    public ReaderResponse toReaderResponce(Reader reader){
        ReaderResponse dto = new ReaderResponse();
        dto.setEmail(reader.getEmail());
        dto.setFullName(reader.getFullName());
        dto.setId(reader.getId());
        dto.setRegistrationDate(reader.getRegistrationDate());
        return dto;
    }
}
