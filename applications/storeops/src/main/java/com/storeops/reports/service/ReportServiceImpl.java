package com.storeops.reports.service;

import com.storeops.common.errors.NotFoundError;
import com.storeops.reports.dto.ReportDto;
import com.storeops.reports.model.Report;
import com.storeops.reports.repository.ReportRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    public ReportServiceImpl(final ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public List<ReportDto> listForStore(final String storeId) {
        return reportRepository.findAllByStoreId(storeId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ReportDto getById(final String id) {
        return reportRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundError("Report not found: " + id));
    }

    private ReportDto toDto(final Report report) {
        return new ReportDto(report.getId(), report.getStoreId(), report.getType(), report.getStatus());
    }
}
