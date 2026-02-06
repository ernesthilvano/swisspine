package com.swisspine.service;

import com.swisspine.dto.ExternalConnectionDTO;
import com.swisspine.dto.PageableResponseDTO;
import com.swisspine.entity.ExternalConnection;
import com.swisspine.exception.BusinessRuleViolationException;
import com.swisspine.exception.ResourceNotFoundException;
import com.swisspine.repository.ExternalConnectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalConnectionServiceTest {

    @Mock
    private ExternalConnectionRepository repository;

    @InjectMocks
    private ExternalConnectionService service;

    @Test
    void findAll_ShouldReturnPagedResults() {
        ExternalConnection conn = ExternalConnection.builder().name("Bloomberg").build();
        conn.setId(1L);
        Page<ExternalConnection> page = new PageImpl<>(Collections.singletonList(conn));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        PageableResponseDTO<ExternalConnectionDTO> result = service.findAll(null, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Bloomberg", result.getContent().get(0).getName());
    }

    @Test
    void findById_ShouldReturnDto_WhenExists() {
        ExternalConnection conn = ExternalConnection.builder().name("Bloomberg").build();
        conn.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(conn));

        ExternalConnectionDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void create_ShouldSaveAndReturnDto() {
        ExternalConnectionDTO dto = ExternalConnectionDTO.builder().name("New Conn").isDefault(true).build();
        ExternalConnection entity = ExternalConnection.builder().name("New Conn").isDefault(true).build();
        entity.setId(1L);

        when(repository.findByNameIgnoreCase("New Conn")).thenReturn(Optional.empty());

        // Mock default toggling
        ExternalConnection existingDefault = ExternalConnection.builder().name("Old Default").isDefault(true)
                .build();
        existingDefault.setId(2L);
        when(repository.findByIsDefaultTrue()).thenReturn(Optional.of(existingDefault));

        when(repository.save(any(ExternalConnection.class))).thenReturn(entity);

        ExternalConnectionDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("New Conn", result.getName());
        verify(repository).save(existingDefault); // Should have been updated to false
        assertFalse(existingDefault.getIsDefault());
    }

    @Test
    void create_ShouldThrowException_WhenNameExists() {
        ExternalConnectionDTO dto = ExternalConnectionDTO.builder().name("Existing").build();
        when(repository.findByNameIgnoreCase("Existing")).thenReturn(Optional.of(new ExternalConnection()));

        assertThrows(BusinessRuleViolationException.class, () -> service.create(dto));
    }

    @Test
    void update_ShouldUpdateAndReturnDto_WhenValid() {
        ExternalConnectionDTO dto = ExternalConnectionDTO.builder()
                .name("Updated Name")
                .baseUrl("http://updated.com")
                .build();

        ExternalConnection existing = ExternalConnection.builder()
                .name("Old Name")
                .baseUrl("http://old.com")
                .build();
        existing.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(ExternalConnection.class))).thenReturn(existing);

        ExternalConnectionDTO result = service.update(1L, dto);

        assertEquals("Updated Name", result.getName());
        assertEquals("http://updated.com", result.getBaseUrl());
    }

    @Test
    void update_ShouldThrowException_WhenNameNotUnique() {
        ExternalConnectionDTO dto = ExternalConnectionDTO.builder().name("Duplicate").build();
        ExternalConnection existing = ExternalConnection.builder().name("Original").build();
        existing.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.countByNameAndIdNot("Duplicate", 1L)).thenReturn(1L);

        assertThrows(BusinessRuleViolationException.class, () -> service.update(1L, dto));
    }

    @Test
    void update_ShouldThrowException_WhenModifyingImmutableValueField() {
        ExternalConnectionDTO dto = ExternalConnectionDTO.builder().valueField("NewSecret").build();
        ExternalConnection existing = ExternalConnection.builder()
                .valueField("OldSecret")
                .valueFieldSet(true)
                .name("Conn")
                .build();
        existing.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(BusinessRuleViolationException.class, () -> service.update(1L, dto));
    }

    @Test
    void update_ShouldAllowValueFieldUpdate_WhenNotPreviouslySet() {
        ExternalConnectionDTO dto = ExternalConnectionDTO.builder().valueField("NewSecret").name("Conn").build();
        ExternalConnection existing = ExternalConnection.builder()
                .valueField(null)
                .valueFieldSet(false)
                .name("Conn")
                .build();
        existing.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(ExternalConnection.class))).thenReturn(existing);

        service.update(1L, dto);

        assertEquals("NewSecret", existing.getValueField());
    }

    @Test
    void delete_ShouldCallRepository_WhenExists() {
        when(repository.existsById(1L)).thenReturn(true);
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(repository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void copy_ShouldCreateCopyDoesNotCopyValue() {
        ExternalConnection existing = ExternalConnection.builder()
                .name("Original")
                .baseUrl("http://url.com")
                .valueField("Secret")
                .valueFieldSet(true)
                .isDefault(true)
                .build();
        existing.setId(1L);

        ExternalConnection copied = ExternalConnection.builder()
                .name("Original (Copy)")
                .isDefault(false)
                .build();
        copied.setId(2L);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(ExternalConnection.class))).thenReturn(copied);

        ExternalConnectionDTO result = service.copy(1L);

        assertEquals("Original (Copy)", result.getName());
        assertFalse(result.getIsDefault());
        assertNull(result.getValueField());
    }
}
