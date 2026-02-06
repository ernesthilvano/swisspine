package com.swisspine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisspine.dto.ExternalConnectionDTO;
import com.swisspine.dto.PageableResponseDTO;
import com.swisspine.service.ExternalConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExternalConnectionControllerTest {

    private MockMvc mockMvc;

    private TestExternalConnectionService externalConnectionService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ExternalConnectionDTO connectionDTO;

    @BeforeEach
    void setUp() {
        externalConnectionService = new TestExternalConnectionService();
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
                .standaloneSetup(new ExternalConnectionController(externalConnectionService))
                .build();

        connectionDTO = ExternalConnectionDTO.builder()
                .id(1L)
                .name("Test Connection")
                .baseUrl("http://localhost:5432")
                .authenticationMethod("API Key")
                .keyField("api_key")
                .authenticationPlace("Header")
                .build();
    }

    @Test
    void getAllConnections_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/external-connections")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getConnectionById_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/external-connections/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Connection"));
    }

    @Test
    void createConnection_ShouldReturnCreated() throws Exception {
        mockMvc.perform(post("/api/external-connections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(connectionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Connection"));
    }

    @Test
    void updateConnection_ShouldReturnOk() throws Exception {
        mockMvc.perform(put("/api/external-connections/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(connectionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Connection"));
    }

    @Test
    void deleteConnection_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/external-connections/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void copyConnection_ShouldReturnCreated() throws Exception {
        mockMvc.perform(post("/api/external-connections/1/copy"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Connection (Copy)"));
    }

    // Manual Stub
    static class TestExternalConnectionService extends ExternalConnectionService {
        public TestExternalConnectionService() {
            super(null);
        }

        @Override
        public PageableResponseDTO<ExternalConnectionDTO> findAll(String search, int page, int size) {
            return PageableResponseDTO.<ExternalConnectionDTO>builder()
                    .content(Collections
                            .singletonList(ExternalConnectionDTO.builder().id(1L).name("Test Connection").build()))
                    .totalElements(1)
                    .totalPages(1)
                    .build();
        }

        @Override
        public ExternalConnectionDTO findById(Long id) {
            return ExternalConnectionDTO.builder().id(id).name("Test Connection").build();
        }

        @Override
        public ExternalConnectionDTO create(ExternalConnectionDTO dto) {
            return dto;
        }

        @Override
        public ExternalConnectionDTO update(Long id, ExternalConnectionDTO dto) {
            return dto;
        }

        @Override
        public void delete(Long id) {
            // no-op
        }

        @Override
        public ExternalConnectionDTO copy(Long id) {
            return ExternalConnectionDTO.builder().id(2L).name("Test Connection (Copy)").build();
        }
    }
}
