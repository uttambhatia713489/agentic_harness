package com.storeops.reports.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.storeops.common.errors.NotFoundError;
import com.storeops.reports.model.Report;
import com.storeops.reports.model.ReportStatus;
import com.storeops.reports.model.ReportType;
import com.storeops.reports.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    private ReportService reportService;

    @BeforeEach
    void setUp() {
        reportService = new ReportServiceImpl(reportRepository);
    }

    @Test
    void testListForStoreReturnsEmptyList() {
        when(reportRepository.findAllByStoreId("store-1")).thenReturn(new ArrayList<>());

        final var result = reportService.listForStore("store-1");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListForStoreReturnsReports() {
        final Report report = new Report();
        report.setId("report-1");
        report.setStoreId("store-1");
        report.setType(ReportType.STORE_SUMMARY);
        report.setStatus(ReportStatus.READY);
        report.setGeneratedAt(Instant.now());
        when(reportRepository.findAllByStoreId("store-1")).thenReturn(Arrays.asList(report));

        final var result = reportService.listForStore("store-1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("report-1", result.get(0).id());
    }

    @Test
    void testGetByIdThrowsNotFoundWhenReportNotExists() {
        when(reportRepository.findById("non-existent")).thenReturn(Optional.empty());

        assertThrows(NotFoundError.class, () -> reportService.getById("non-existent"));
    }

    @Test
    void testGetByIdReturnsReport() {
        final Report report = new Report();
        report.setId("report-1");
        report.setStoreId("store-1");
        report.setType(ReportType.REGIONAL_ROLLUP);
        report.setStatus(ReportStatus.READY);
        report.setGeneratedAt(Instant.now());
        when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));

        final var result = reportService.getById("report-1");

        assertNotNull(result);
        assertEquals("report-1", result.id());
        assertEquals(ReportType.REGIONAL_ROLLUP, result.type());
    }
}
