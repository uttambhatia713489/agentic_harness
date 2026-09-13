package com.storeops.activities.repository;

import com.storeops.activities.model.Task;
import com.storeops.activities.model.TaskStatus;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryActivityRepository implements ActivityRepository {

    private final Map<String, Task> store = new ConcurrentHashMap<>();

    @Override
    public List<Task> findAll(final String programmeId, final TaskStatus status) {
        return store.values().stream()
                .filter(task -> programmeId == null || programmeId.equals(task.getProgrammeId()))
                .filter(task -> status == null || status.equals(task.getStatus()))
                .toList();
    }

    @Override
    public Optional<Task> findById(final String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Task save(final Task task) {
        store.put(task.getId(), task);
        return task;
    }

    @Override
    public void deleteById(final String id) {
        store.remove(id);
    }
}
