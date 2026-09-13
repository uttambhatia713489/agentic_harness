package com.storeops.reports.repository;

import com.storeops.reports.model.Report;
import java.util.List;
import java.util.Optional;

public interface ReportRepository {

    List<Report> findAllByStoreId(String storeId);

    Optional<Report> findById(String id);
}
