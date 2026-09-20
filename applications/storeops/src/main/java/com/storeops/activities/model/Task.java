package com.storeops.activities.model;

import java.time.Instant;

public class Task {

    private String id;
    private String programmeId;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private TaskCategory category;
    private String assigneeId;
    private String ownerId;
    private Instant dueAt;
    private Instant createdAt;
    private Instant updatedAt;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(final String programmeId) {
        this.programmeId = programmeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(final TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(final TaskPriority priority) {
        this.priority = priority;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public void setCategory(final TaskCategory category) {
        this.category = category;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(final String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(final String ownerId) {
        this.ownerId = ownerId;
    }

    public Instant getDueAt() {
        return dueAt;
    }

    public void setDueAt(final Instant dueAt) {
        this.dueAt = dueAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
