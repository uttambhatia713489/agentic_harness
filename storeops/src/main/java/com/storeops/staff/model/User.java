package com.storeops.staff.model;

public class User {

    private String id;
    private String email;
    private String passwordHash;
    private StaffRole role;
    private UserProfile profile;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(final String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(final StaffRole role) {
        this.role = role;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(final UserProfile profile) {
        this.profile = profile;
    }
}
