package com.storeops.activities.service;

import com.storeops.activities.model.Task;
import com.storeops.activities.model.TaskPriority;
import com.storeops.activities.model.TaskStatus;
import com.storeops.activities.repository.ActivityRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class SlaBreachEvaluationServiceImpl implements SlaBreachEvaluationService {

    private static final Set<TaskPriority> ELIGIBLE_PRIORITIES = Set.of(TaskPriority.HIGH, TaskPriority.CRITICAL);

    private final ActivityRepository activityRepository;
    private final Clock clock;

    public SlaBreachEvaluationServiceImpl(final ActivityRepository activityRepository, final Clock clock) {
        this.activityRepository = activityRepository;
        this.clock = clock;
    }

    @Override
    public boolean isBreach(final Task task) {
        if (task == null || task.getDueAt() == null) {
            return false;
        }
        if (!ELIGIBLE_PRIORITIES.contains(task.getPriority())) {
            return false;
        }
        if (task.getStatus() == TaskStatus.DONE) {
            return false;
        }
        return task.getDueAt().isBefore(Instant.now(clock));
    }

    @Override
    public List<Task> detectBreaches() {
        return activityRepository.findAll(null, null).stream()
                .filter(this::isBreach)
                .toList();
    }
}
