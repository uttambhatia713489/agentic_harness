package com.storeops.staff.dto;

import com.storeops.staff.model.StaffRole;

public record UserDto(String id, String email, StaffRole role, String displayName, String storeId) {
}
