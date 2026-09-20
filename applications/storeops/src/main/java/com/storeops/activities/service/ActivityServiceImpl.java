package com.storeops.activities.service;

import com.storeops.activities.dto.ActivityDto;
import com.storeops.activities.dto.CreateActivityRequest;
import com.storeops.activities.dto.UpdateActivityRequest;
import com.storeops.activities.model.Task;
import com.storeops.activities.model.TaskStatus;
import com.storeops.activities.events.TaskOverdueEvent;
import com.storeops.activities.repository.ActivityRepository;
import com.storeops.common.errors.NotFoundError;
import com.storeops.common.events.EventBus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final EventBus eventBus;

    public ActivityServiceImpl(final ActivityRepository activityRepository, final EventBus eventBus) {
        this.activityRepository = activityRepository;
        this.eventBus = eventBus;
    }

    @Override
    public List<ActivityDto> list(final String programmeId, final TaskStatus status) {
        return activityRepository.findAll(programmeId, status).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ActivityDto create(final CreateActivityRequest request) {
        final Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setProgrammeId(request.programmeId());
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(TaskStatus.TODO);
        task.setPriority(request.priority());
        task.setCategory(request.category());
        task.setAssigneeId(request.assigneeId());
        task.setDueAt(request.dueAt());
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        return toDto(activityRepository.save(task));
    }

    @Override
    public ActivityDto getById(final String id) {
        return activityRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundError("Activity not found: " + id));
    }

    @Override
    public ActivityDto update(final String id, final UpdateActivityRequest request) {
        final Task task = activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundError("Activity not found: " + id));
        if (request.status() != null) {
            task.setStatus(request.status());
        }
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }
        if (request.category() != null) {
            task.setCategory(request.category());
        }
        if (request.assigneeId() != null) {
            task.setAssigneeId(request.assigneeId());
        }
        if (request.dueAt() != null) {
            task.setDueAt(request.dueAt());
        }
        task.setUpdatedAt(Instant.now());
        final Task saved = activityRepository.save(task);
        if (saved.getStatus() == TaskStatus.BLOCKED) {
            eventBus.emit(new TaskOverdueEvent(saved.getId(), saved.getProgrammeId()));
        }
        return toDto(saved);
    }

    @Override
    public void delete(final String id, final String requesterId) {
        final Task task = activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundError("Activity not found: " + id));
        activityRepository.deleteById(task.getId());
    }

    private ActivityDto toDto(final Task task) {
        return new ActivityDto(
                task.getId(),
                task.getProgrammeId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getCategory(),
                task.getAssigneeId(),
                task.getOwnerId(),
                task.getDueAt()
        );
    }
}
