package com.swisspine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisspine.config.JpaConfiguration;
import com.swisspine.dto.PageableResponseDTO;
import com.swisspine.dto.PlannerDTO;
import com.swisspine.service.PlannerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

class PlannerControllerTest {

    private MockMvc mockMvc;

    private TestPlannerService plannerService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private PlannerDTO plannerDTO;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        plannerService = new TestPlannerService();
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
                .standaloneSetup(new PlannerController(plannerService))
                .build();

        plannerDTO = PlannerDTO.builder()
                .id(1L)
                .name("Test Planner")
                .description("Test Description")
                .status("Draft")
                .build();
    }

    @Test
    void getAllPlanners_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/planners")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void searchPlanners_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/planners/search")
                .param("q", "Test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getPlannerById_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/planners/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Planner"));
    }

    @Test
    void createPlanner_ShouldReturnCreated() throws Exception {
        mockMvc.perform(post("/api/planners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(plannerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Planner"));
    }

    @Test
    void updatePlanner_ShouldReturnOk() throws Exception {
        mockMvc.perform(put("/api/planners/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(plannerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Planner"));
    }

    @Test
    void deletePlanner_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/planners/1"))
                .andExpect(status().isNoContent());
    }

    // Manual Stub
    static class TestPlannerService extends PlannerService {
        public TestPlannerService() {
            super(null, null);
        }

        @Override
        public PageableResponseDTO<PlannerDTO> findAll(String status, int page, int size) {
            return PageableResponseDTO.<PlannerDTO>builder()
                    .content(Collections.singletonList(PlannerDTO.builder().id(1L).name("Test Planner").build()))
                    .totalElements(1)
                    .totalPages(1)
                    .build();
        }

        @Override
        public PageableResponseDTO<PlannerDTO> search(String query, String status, int page, int size) {
            return PageableResponseDTO.<PlannerDTO>builder()
                    .content(Collections.singletonList(PlannerDTO.builder().id(1L).name("Test Planner").build()))
                    .build();
        }

        @Override
        public PlannerDTO findById(Long id) {
            return PlannerDTO.builder().id(id).name("Test Planner").build();
        }

        @Override
        public PlannerDTO create(PlannerDTO dto) {
            return dto;
        }

        @Override
        public PlannerDTO update(Long id, PlannerDTO dto) {
            return dto;
        }

        @Override
        public void delete(Long id) {
            // no-op
        }
    }
}
