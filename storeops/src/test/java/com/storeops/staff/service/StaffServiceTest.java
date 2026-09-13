package com.storeops.staff.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.storeops.common.errors.NotFoundError;
import com.storeops.common.errors.UnauthorizedError;
import com.storeops.staff.dto.LoginRequest;
import com.storeops.staff.model.StaffRole;
import com.storeops.staff.model.User;
import com.storeops.staff.model.UserProfile;
import com.storeops.staff.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    private StaffService staffService;

    @BeforeEach
    void setUp() {
        staffService = new StaffServiceImpl(staffRepository);
    }

    @Test
    void testLoginThrowsUnauthorizedWhenUserNotFound() {
        when(staffRepository.findByEmail("invalid@test.com")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedError.class, () ->
                staffService.login(new LoginRequest("invalid@test.com", "password")));
    }

    @Test
    void testLoginReturnsTokenAndUserDto() {
        final User user = new User();
        user.setId("user-1");
        user.setEmail("admin@test.com");
        user.setRole(StaffRole.STORE_MANAGER);
        final UserProfile profile = new UserProfile();
        profile.setDisplayName("Admin");
        profile.setStoreId("store-1");
        user.setProfile(profile);
        when(staffRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(user));

        final var result = staffService.login(new LoginRequest("admin@test.com", "password"));

        assertNotNull(result);
        assertNotNull(result.token());
        assertNotNull(result.user());
        assertEquals("admin@test.com", result.user().email());
        assertEquals(StaffRole.STORE_MANAGER, result.user().role());
    }

    @Test
    void testGetByIdThrowsNotFoundWhenUserNotExists() {
        when(staffRepository.findById("non-existent")).thenReturn(Optional.empty());

        assertThrows(NotFoundError.class, () -> staffService.getById("non-existent"));
    }

    @Test
    void testGetByIdReturnsUserDto() {
        final User user = new User();
        user.setId("user-1");
        user.setEmail("user@test.com");
        user.setRole(StaffRole.ASSOCIATE);
        final UserProfile profile = new UserProfile();
        profile.setDisplayName("John Doe");
        profile.setStoreId("store-2");
        user.setProfile(profile);
        when(staffRepository.findById("user-1")).thenReturn(Optional.of(user));

        final var result = staffService.getById("user-1");

        assertNotNull(result);
        assertEquals("user-1", result.id());
        assertEquals("user@test.com", result.email());
        assertEquals("John Doe", result.displayName());
    }

    @Test
    void testLoginWithoutProfile() {
        final User user = new User();
        user.setId("user-2");
        user.setEmail("noProfile@test.com");
        user.setRole(StaffRole.DEPARTMENT_LEAD);
        when(staffRepository.findByEmail("noProfile@test.com")).thenReturn(Optional.of(user));

        final var result = staffService.login(new LoginRequest("noProfile@test.com", "password"));

        assertNotNull(result);
        assertNull(result.user().displayName());
        assertNull(result.user().storeId());
    }
}
