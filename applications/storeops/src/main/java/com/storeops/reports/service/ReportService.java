package com.storeops.reports.service;

import com.storeops.reports.dto.ReportDto;
import java.util.List;

public interface ReportService {

    List<ReportDto> listForStore(String storeId);

    ReportDto getById(String id);
}
