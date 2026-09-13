package com.storeops.reports.controller;

import com.storeops.reports.dto.ReportDto;
import com.storeops.reports.service.ReportService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(final ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<ReportDto>> list(@RequestHeader(name = "X-Store-Id") final String storeId) {
        return ResponseEntity.ok(reportService.listForStore(storeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDto> getById(@PathVariable final String id) {
        return ResponseEntity.ok(reportService.getById(id));
    }
}
