package com.storeops.reports.repository;

import com.storeops.reports.model.Report;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryReportRepository implements ReportRepository {

    private final Map<String, Report> store = new ConcurrentHashMap<>();

    @Override
    public List<Report> findAllByStoreId(final String storeId) {
        return store.values().stream()
                .filter(report -> storeId == null || storeId.equals(report.getStoreId()))
                .toList();
    }

    @Override
    public Optional<Report> findById(final String id) {
        return Optional.ofNullable(store.get(id));
    }
}
