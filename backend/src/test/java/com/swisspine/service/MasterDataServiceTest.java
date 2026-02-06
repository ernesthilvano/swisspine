package com.swisspine.service;

import com.swisspine.dto.MasterDataDTO;
import com.swisspine.entity.*;
import com.swisspine.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MasterDataServiceTest {

    @Mock
    private FundRepository fundRepository;
    @Mock
    private FundAliasRepository fundAliasRepository; // Not used yet but present in Service
    @Mock
    private SourceNameRepository sourceNameRepository;
    @Mock
    private RunNameRepository runNameRepository;
    @Mock
    private ReportTypeRepository reportTypeRepository;
    @Mock
    private ReportNameRepository reportNameRepository;

    @InjectMocks
    private MasterDataService service;

    // --- Fund Tests ---
    @Test
    void getAllFunds_ShouldReturnList() {
        Fund fund = Fund.builder().name("Fund A").build();
        fund.setId(1L);
        when(fundRepository.findAllByOrderByNameAsc()).thenReturn(Collections.singletonList(fund));

        List<MasterDataDTO> result = service.getAllFunds();

        assertEquals(1, result.size());
        assertEquals("Fund A", result.get(0).getName());
    }

    @Test
    void createFund_ShouldSaveAndReturnDto() {
        MasterDataDTO dto = MasterDataDTO.builder().name("New Fund").build();
        Fund entity = Fund.builder().name("New Fund").build();
        entity.setId(1L);
        when(fundRepository.save(any(Fund.class))).thenReturn(entity);

        MasterDataDTO result = service.createFund(dto);

        assertEquals("New Fund", result.getName());
    }

    @Test
    void deleteFund_ShouldCallRepository() {
        service.deleteFund(1L);
        verify(fundRepository).deleteById(1L);
    }

    // --- SourceName Tests ---
    @Test
    void getAllSourceNames_ShouldReturnList() {
        SourceName entity = new SourceName();
        entity.setName("Source A");
        entity.setId(1L);
        when(sourceNameRepository.findAllByOrderByNameAsc()).thenReturn(Collections.singletonList(entity));

        List<MasterDataDTO> result = service.getAllSourceNames();

        assertEquals(1, result.size());
        assertEquals("Source A", result.get(0).getName());
    }

    @Test
    void createSourceName_ShouldSaveAndReturnDto() {
        MasterDataDTO dto = MasterDataDTO.builder().name("New Source").build();
        SourceName entity = new SourceName();
        entity.setName("New Source");
        entity.setId(1L);
        when(sourceNameRepository.save(any(SourceName.class))).thenReturn(entity);

        MasterDataDTO result = service.createSourceName(dto);

        assertEquals("New Source", result.getName());
    }

    @Test
    void deleteSourceName_ShouldCallRepository() {
        service.deleteSourceName(1L);
        verify(sourceNameRepository).deleteById(1L);
    }

    // --- RunName Tests ---
    @Test
    void getAllRunNames_ShouldReturnList() {
        RunName entity = new RunName();
        entity.setName("Run A");
        entity.setId(1L);
        when(runNameRepository.findAllByOrderByNameAsc()).thenReturn(Collections.singletonList(entity));

        List<MasterDataDTO> result = service.getAllRunNames();

        assertEquals(1, result.size());
        assertEquals("Run A", result.get(0).getName());
    }

    @Test
    void createRunName_ShouldSaveAndReturnDto() {
        MasterDataDTO dto = MasterDataDTO.builder().name("New Run").build();
        RunName entity = new RunName();
        entity.setName("New Run");
        entity.setId(1L);
        when(runNameRepository.save(any(RunName.class))).thenReturn(entity);

        MasterDataDTO result = service.createRunName(dto);

        assertEquals("New Run", result.getName());
    }

    @Test
    void deleteRunName_ShouldCallRepository() {
        service.deleteRunName(1L);
        verify(runNameRepository).deleteById(1L);
    }

    // --- ReportType Tests ---
    @Test
    void getAllReportTypes_ShouldReturnList() {
        ReportType entity = new ReportType();
        entity.setName("Type A");
        entity.setId(1L);
        when(reportTypeRepository.findAllByOrderByNameAsc()).thenReturn(Collections.singletonList(entity));

        List<MasterDataDTO> result = service.getAllReportTypes();

        assertEquals(1, result.size());
        assertEquals("Type A", result.get(0).getName());
    }

    @Test
    void createReportType_ShouldSaveAndReturnDto() {
        MasterDataDTO dto = MasterDataDTO.builder().name("New Type").build();
        ReportType entity = new ReportType();
        entity.setName("New Type");
        entity.setId(1L);
        when(reportTypeRepository.save(any(ReportType.class))).thenReturn(entity);

        MasterDataDTO result = service.createReportType(dto);

        assertEquals("New Type", result.getName());
    }

    @Test
    void deleteReportType_ShouldCallRepository() {
        service.deleteReportType(1L);
        verify(reportTypeRepository).deleteById(1L);
    }

    // --- ReportName Tests ---
    @Test
    void getAllReportNames_ShouldReturnList() {
        ReportName entity = new ReportName();
        entity.setName("Report A");
        entity.setId(1L);
        when(reportNameRepository.findAllByOrderByNameAsc()).thenReturn(Collections.singletonList(entity));

        List<MasterDataDTO> result = service.getAllReportNames();

        assertEquals(1, result.size());
        assertEquals("Report A", result.get(0).getName());
    }

    @Test
    void getReportNamesByType_ShouldReturnFilteredList() {
        ReportName entity = new ReportName();
        entity.setName("Report A");
        entity.setId(1L);
        when(reportNameRepository.findByReportTypeIdOrderByNameAsc(10L)).thenReturn(Collections.singletonList(entity));

        List<MasterDataDTO> result = service.getReportNamesByType(10L);

        assertEquals(1, result.size());
        assertEquals("Report A", result.get(0).getName());
    }

    @Test
    void createReportName_ShouldSaveAndReturnDto() {
        MasterDataDTO dto = MasterDataDTO.builder().name("New Report").build();
        ReportName entity = new ReportName();
        entity.setName("New Report");
        entity.setId(1L);
        when(reportNameRepository.save(any(ReportName.class))).thenReturn(entity);

        MasterDataDTO result = service.createReportName(dto);

        assertEquals("New Report", result.getName());
    }

    @Test
    void deleteReportName_ShouldCallRepository() {
        service.deleteReportName(1L);
        verify(reportNameRepository).deleteById(1L);
    }
}
