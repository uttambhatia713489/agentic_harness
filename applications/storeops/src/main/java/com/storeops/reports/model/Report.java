package com.storeops.reports.model;

import java.time.Instant;

public class Report {

    private String id;
    private String storeId;
    private ReportType type;
    private ReportStatus status;
    private Instant generatedAt;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(final String storeId) {
        this.storeId = storeId;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(final ReportType type) {
        this.type = type;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(final ReportStatus status) {
        this.status = status;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(final Instant generatedAt) {
        this.generatedAt = generatedAt;
    }
}
