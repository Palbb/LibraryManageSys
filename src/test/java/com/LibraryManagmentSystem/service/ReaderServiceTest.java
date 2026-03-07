package com.LibraryManagmentSystem.service;

import com.LibraryManagmentSystem.Entities.Reader;
import com.LibraryManagmentSystem.dto.ReaderCreateRequest;
import com.LibraryManagmentSystem.dto.ReaderResponce;
import com.LibraryManagmentSystem.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)

class ReaderServiceTest {

    Reader reader1 = new Reader();

    @Mock
    ReaderRepository readerRepository;

    @InjectMocks
    ReaderService readerService;

    @BeforeEach
     void setup(){
        reader1.setId(10L);
        reader1.setEmail("SomeDude@example.com");
        reader1.setFullName("Some Dude");
        reader1.setRegistrationDate(LocalDate.now());
    }

    @Test
    void getReaderByEmailSuccess() {
        String email = "SomeDude@example.com";

        when(readerRepository.findByEmailIs(email)).thenReturn(Optional.of(reader1));

        ReaderResponce result = readerService.getReaderByEmail(email);

        assertNotNull(result);
        assertEquals(10L , result.getId());
        assertEquals("SomeDude@example.com", result.getEmail());

        verify(readerRepository, Mockito.times(1)).findByEmailIs(email);

    }

    @Test
    void getReaderByEmailFailure() {
        String email = "SomeDude@example.com";

        when(readerRepository.findByEmailIs(email)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            readerService.getReaderByEmail(email);
        });

        verify(readerRepository, Mockito.times(1)).findByEmailIs(email);

    }

    @Test
    void getByFullNameSuccess() {
        String fullName = "Some Dude";

        when(readerRepository.findByFullNameContainingIgnoreCase(fullName)).thenReturn(Optional.of(reader1));

        ReaderResponce result = readerService.getByFullName(fullName);

        assertNotNull(result);
        assertEquals("Some Dude" , result.getFullName());

        verify(readerRepository, Mockito.times(1)).findByFullNameContainingIgnoreCase(fullName);

    }


    @Test
    void getByFullNameFailure() {
        String fullName = "Some Dude";

        when(readerRepository.findByFullNameContainingIgnoreCase(fullName)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            readerService.getByFullName(fullName);
        });

        verify(readerRepository, Mockito.times(1)).findByFullNameContainingIgnoreCase(fullName);

    }

    @Test
    void createReaderSuccess() {
        ReaderCreateRequest request = new ReaderCreateRequest();
        request.setFullName("Some Dude");
        request.setEmail("SomeDude@example.com");
        Reader readerToSave = new Reader();
        readerToSave.setId(10L);
        readerToSave.setEmail(request.getEmail());
        readerToSave.setFullName(request.getFullName());
        readerToSave.setRegistrationDate(LocalDate.now());

        when(readerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(readerRepository.save(any(Reader.class))).thenReturn(readerToSave);
        var result = readerService.createReader(request);

        assertNotNull(result);
        assertEquals("SomeDude@example.com" , result.getEmail());
        assertEquals(10L,result.getId());

        verify(readerRepository, Mockito.times(1)).save(any(Reader.class));

    }

    @Test
    void createReaderFailure() {
        ReaderCreateRequest request = new ReaderCreateRequest();
        request.setFullName("Some Dudes");
        request.setEmail("SomeDudes@example.com");

        when(readerRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            readerService.createReader(request);
        });
        verify(readerRepository, never()).save(any());
    }

    @Test
    void deleteReaderSuccess() {
        Long id = reader1.getId();

        when(readerRepository.existsById(id)).thenReturn(true);
        readerService.deleteReader(id);

        verify(readerRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteReaderFailure() {
        Long id = reader1.getId();

        when(readerRepository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            readerService.deleteReader(id);
        });

        verify(readerRepository, never()).deleteById(id);
    }
}