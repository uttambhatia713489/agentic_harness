package com.storeops.programmes.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Project {

    private String id;
    private String storeId;
    private String name;
    private String description;
    private List<ProjectMember> members = new ArrayList<>();
    private Instant createdAt;
    private Instant closedAt;

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

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<ProjectMember> getMembers() {
        return members;
    }

    public void setMembers(final List<ProjectMember> members) {
        this.members = members;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(final Instant closedAt) {
        this.closedAt = closedAt;
    }
}
