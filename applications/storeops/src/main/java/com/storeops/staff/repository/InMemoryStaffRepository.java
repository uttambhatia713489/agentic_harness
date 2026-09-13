package com.storeops.staff.repository;

import com.storeops.staff.model.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryStaffRepository implements StaffRepository {

    private final Map<String, User> store = new ConcurrentHashMap<>();

    @Override
    public Optional<User> findByEmail(final String email) {
        return store.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public Optional<User> findById(final String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public User save(final User user) {
        store.put(user.getId(), user);
        return user;
    }
}
