package com.storeops.staff.service;

import com.storeops.staff.dto.LoginRequest;
import com.storeops.staff.dto.LoginResponse;
import com.storeops.staff.dto.UserDto;

public interface StaffService {

    LoginResponse login(LoginRequest request);

    UserDto getById(String id);
}
