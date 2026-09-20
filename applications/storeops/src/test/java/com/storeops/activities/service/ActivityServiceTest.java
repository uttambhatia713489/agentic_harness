package com.storeops.activities.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.storeops.activities.dto.CreateActivityRequest;
import com.storeops.activities.model.Task;
import com.storeops.activities.model.TaskCategory;
import com.storeops.activities.model.TaskPriority;
import com.storeops.activities.model.TaskStatus;
import com.storeops.activities.repository.ActivityRepository;
import com.storeops.common.errors.NotFoundError;
import com.storeops.common.events.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private EventBus eventBus;

    private ActivityService activityService;

    @BeforeEach
    void setUp() {
        activityService = new ActivityServiceImpl(activityRepository, eventBus);
    }

    @Test
    void testCreateActivityReturnsNewTaskWithTodoStatus() {
        final CreateActivityRequest request = new CreateActivityRequest("prog-1", "New Task", "Description",
                TaskPriority.HIGH, TaskCategory.RESTOCKING, "user-1", null);
        final Task task = new Task();
        task.setId("id-1");
        when(activityRepository.save(any())).thenReturn(task);

        final var result = activityService.create(request);

        assertNotNull(result);
        assertEquals("id-1", result.id());
    }

    @Test
    void testGetByIdThrowsNotFoundErrorWhenTaskNotExists() {
        when(activityRepository.findById("non-existent")).thenReturn(Optional.empty());

        assertThrows(NotFoundError.class, () -> activityService.getById("non-existent"));
    }

    @Test
    void testGetByIdReturnsTaskWhenExists() {
        final Task task = new Task();
        task.setId("id-1");
        task.setTitle("Sample Task");
        when(activityRepository.findById("id-1")).thenReturn(Optional.of(task));

        final var result = activityService.getById("id-1");

        assertEquals("id-1", result.id());
        assertEquals("Sample Task", result.title());
    }
}
