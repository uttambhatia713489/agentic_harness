package com.storeops.staff.service;

import com.storeops.common.errors.NotFoundError;
import com.storeops.common.errors.UnauthorizedError;
import com.storeops.staff.dto.LoginRequest;
import com.storeops.staff.dto.LoginResponse;
import com.storeops.staff.dto.UserDto;
import com.storeops.staff.model.AuthToken;
import com.storeops.staff.model.User;
import com.storeops.staff.repository.StaffRepository;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final ConcurrentHashMap<String, AuthToken> tokens = new ConcurrentHashMap<>();

    public StaffServiceImpl(final StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public LoginResponse login(final LoginRequest request) {
        final User user = staffRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedError("Invalid credentials"));
        final AuthToken authToken = new AuthToken(
                UUID.randomUUID().toString(),
                user.getId(),
                Instant.now().plus(1, TimeUnit.HOURS.toChronoUnit()));
        tokens.put(authToken.token(), authToken);
        return new LoginResponse(authToken.token(), toDto(user));
    }

    @Override
    public UserDto getById(final String id) {
        return staffRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundError("User not found: " + id));
    }

    private UserDto toDto(final User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getProfile() == null ? null : user.getProfile().getDisplayName(),
                user.getProfile() == null ? null : user.getProfile().getStoreId());
    }
}
