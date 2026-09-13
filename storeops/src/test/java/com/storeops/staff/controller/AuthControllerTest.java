package com.storeops.staff.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeops.staff.dto.LoginRequest;
import com.storeops.staff.dto.LoginResponse;
import com.storeops.staff.dto.UserDto;
import com.storeops.staff.model.StaffRole;
import com.storeops.staff.service.StaffService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StaffService staffService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoginSuccessful() throws Exception {
        final LoginRequest request = new LoginRequest("user@test.com", "password");
        final UserDto userDto = new UserDto("user-1", "user@test.com", StaffRole.STORE_MANAGER, "John", "store-1");
        final LoginResponse response = new LoginResponse("token-123", userDto);
        when(staffService.login(any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-123"))
                .andExpect(jsonPath("$.user.email").value("user@test.com"))
                .andExpect(jsonPath("$.user.role").value("STORE_MANAGER"));
    }

    @Test
    void testLoginWithMissingEmail() throws Exception {
        final LoginRequest request = new LoginRequest("", "password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithMissingPassword() throws Exception {
        final LoginRequest request = new LoginRequest("user@test.com", "");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
