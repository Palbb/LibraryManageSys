package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.Entities.Reader;
import com.LibraryManagmentSystem.dto.ReaderCreateRequest;
import com.LibraryManagmentSystem.dto.ReaderResponce;
import com.LibraryManagmentSystem.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReaderService {
    private ReaderRepository readerRepository;
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public ReaderResponce getReaderByEmail(String email){

        return toReaderResponce(readerRepository.findByEmailIs(email).
                orElseThrow(() -> new NoSuchElementException("Reader with this email" + email + " does not exist ")));
    }

    public ReaderResponce getById(Long id){
        return toReaderResponce( readerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reader with this id" + id + " does not exists")));
    }

    public ReaderResponce getByFullName(String fullName){
        return toReaderResponce( readerRepository.findByFullNameContainingIgnoreCase(fullName)
                .orElseThrow(() -> new NoSuchElementException("Reader with this fullname" + fullName + " does not exist ")));

    }

    public ReaderResponce createReader(ReaderCreateRequest dto){
        if (readerRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Reader with this email already exists");
        }
        Reader reader = new Reader();
        reader.setEmail(dto.getEmail());
        reader.setFullName(dto.getFullName());
        var save =readerRepository.save(reader);
        return toReaderResponce(save);
    }

    public void deleteReader(Long id) {
        if (!readerRepository.existsById(id)){
            throw new IllegalArgumentException("Reader with id " + id + " not found");
        }
        readerRepository.deleteById(id);
    }
    public ReaderResponce toReaderResponce(Reader reader){
        ReaderResponce dto = new ReaderResponce();
        dto.setEmail(reader.getEmail());
        dto.setFullName(reader.getFullName());
        dto.setId(reader.getId());
        return dto;
    }
}
