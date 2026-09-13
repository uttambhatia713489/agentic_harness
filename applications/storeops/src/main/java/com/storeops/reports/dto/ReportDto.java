package com.storeops.reports.dto;

import com.storeops.reports.model.ReportStatus;
import com.storeops.reports.model.ReportType;

public record ReportDto(String id, String storeId, ReportType type, ReportStatus status) {
}
