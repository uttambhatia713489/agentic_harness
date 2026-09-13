package com.storeops.staff.dto;

public record LoginResponse(String token, UserDto user) {
}
