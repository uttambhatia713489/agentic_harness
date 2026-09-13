package com.storeops.activities.repository;

import com.storeops.activities.model.Task;
import com.storeops.activities.model.TaskStatus;
import java.util.List;
import java.util.Optional;

public interface ActivityRepository {

    List<Task> findAll(String programmeId, TaskStatus status);

    Optional<Task> findById(String id);

    Task save(Task task);

    void deleteById(String id);
}
