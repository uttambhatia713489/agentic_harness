package com.storeops.programmes.model;

public class ProjectMember {

    private String userId;
    private ProjectRole role;

    public ProjectMember() {
    }

    public ProjectMember(final String userId, final ProjectRole role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public ProjectRole getRole() {
        return role;
    }

    public void setRole(final ProjectRole role) {
        this.role = role;
    }
}
