package com.storeops.reports.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.storeops.reports.dto.ReportDto;
import com.storeops.reports.model.ReportStatus;
import com.storeops.reports.model.ReportType;
import com.storeops.reports.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Arrays;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    void testListReportsForStore() throws Exception {
        final ReportDto report = new ReportDto("report-1", "store-1", ReportType.STORE_SUMMARY, ReportStatus.READY);
        when(reportService.listForStore("store-1")).thenReturn(Arrays.asList(report));

        mockMvc.perform(get("/api/reports")
                .header("X-Store-Id", "store-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("report-1"))
                .andExpect(jsonPath("$[0].type").value("STORE_SUMMARY"));
    }

    @Test
    void testGetReportById() throws Exception {
        final ReportDto report = new ReportDto("report-1", "store-1", ReportType.REGIONAL_ROLLUP, ReportStatus.PENDING);
        when(reportService.getById("report-1")).thenReturn(report);

        mockMvc.perform(get("/api/reports/report-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("report-1"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testListReportsEmptyForStore() throws Exception {
        when(reportService.listForStore("store-2")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/reports")
                .header("X-Store-Id", "store-2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
