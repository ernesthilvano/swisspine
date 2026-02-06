package com.swisspine.service;

import com.swisspine.dto.PageableResponseDTO;
import com.swisspine.dto.PlannerDTO;
import com.swisspine.entity.Planner;
import com.swisspine.exception.ResourceNotFoundException;
import com.swisspine.repository.ExternalConnectionRepository;
import com.swisspine.repository.PlannerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlannerServiceTest {

    @Mock
    private PlannerRepository repository;

    @Mock
    private ExternalConnectionRepository connectionRepository;

    @InjectMocks
    private PlannerService plannerService;

    @Test
    void findAll_ShouldReturnPagedResults() {
        // Arrange
        Planner planner = Planner.builder().name("Test Planner").build();
        planner.setId(1L);
        Page<Planner> page = new PageImpl<>(Collections.singletonList(planner));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        PageableResponseDTO<PlannerDTO> result = plannerService.findAll(null, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Planner", result.getContent().get(0).getName());
    }

    @Test
    void search_ShouldInvokeSearchRepositoryMethod() {
        // Arrange
        String query = "finance";
        Planner planner = Planner.builder().name("Finance Plan").build();
        planner.setId(1L);
        Page<Planner> page = new PageImpl<>(Collections.singletonList(planner));
        when(repository.searchByName(eq(query), any(Pageable.class))).thenReturn(page);

        // Act
        PageableResponseDTO<PlannerDTO> result = plannerService.search(query, null, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getSize());
        verify(repository).searchByName(eq(query), any(Pageable.class));
    }

    @Test
    void findById_ShouldReturnPlanner_WhenExists() {
        // Arrange
        Long id = 1L;
        Planner planner = Planner.builder().name("Test").build();
        planner.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(planner));

        // Act
        PlannerDTO result = plannerService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        // Arrange
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> plannerService.findById(id));
    }

    @Test
    void create_ShouldSaveAndReturnPlanner() {
        // Arrange
        PlannerDTO dto = PlannerDTO.builder().name("New Plan").plannerType("Weekly").build();
        Planner entity = Planner.builder().name("New Plan").plannerType("Weekly").status("Draft").build();
        entity.setId(1L);

        when(repository.save(any(Planner.class))).thenReturn(entity);

        // Act
        PlannerDTO result = plannerService.create(dto);

        // Assert
        assertNotNull(result);
        assertEquals("New Plan", result.getName());
        assertEquals("Draft", result.getStatus()); // Default value check
    }

    @Test
    void delete_ShouldRemoveEntity_WhenExists() {
        // Arrange
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        // Act
        plannerService.delete(id);

        // Assert
        verify(repository).deleteById(id);
    }
}
