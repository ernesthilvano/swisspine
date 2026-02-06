package com.swisspine.controller;

import com.swisspine.dto.MasterDataDTO;
import com.swisspine.service.MasterDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MasterDataControllerTest {

    private MockMvc mockMvc;

    private TestMasterDataService masterDataService;

    @BeforeEach
    void setUp() {
        masterDataService = new TestMasterDataService();
        mockMvc = MockMvcBuilders.standaloneSetup(new MasterDataController(masterDataService))
                .build();
    }

    @Test
    void getAllFunds_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/master-data/funds")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllSourceNames_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/master-data/source-names")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllRunNames_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/master-data/run-names")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllReportTypes_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/master-data/report-types")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllReportNames_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/master-data/report-names")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // Manual Stub
    static class TestMasterDataService extends MasterDataService {
        public TestMasterDataService() {
            super(null, null, null, null, null);
        }

        @Override
        public List<MasterDataDTO> getAllFunds() {
            return Collections.singletonList(MasterDataDTO.builder().id(1L).name("Test Fund").build());
        }

        @Override
        public List<MasterDataDTO> getAllSourceNames() {
            return Collections.singletonList(MasterDataDTO.builder().id(1L).name("Test Source").build());
        }

        @Override
        public List<MasterDataDTO> getAllRunNames() {
            return Collections.singletonList(MasterDataDTO.builder().id(1L).name("Test Run").build());
        }

        @Override
        public List<MasterDataDTO> getAllReportTypes() {
            return Collections.singletonList(MasterDataDTO.builder().id(1L).name("Test Report Type").build());
        }

        @Override
        public List<MasterDataDTO> getAllReportNames() {
            return Collections.singletonList(MasterDataDTO.builder().id(1L).name("Test Report Name").build());
        }
    }
}
