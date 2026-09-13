package com.storeops.staff.repository;

import com.storeops.staff.model.User;
import java.util.Optional;

public interface StaffRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);

    User save(User user);
}
