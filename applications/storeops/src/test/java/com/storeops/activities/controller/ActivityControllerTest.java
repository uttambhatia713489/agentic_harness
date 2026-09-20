package com.storeops.activities.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeops.activities.dto.ActivityDto;
import com.storeops.activities.dto.CreateActivityRequest;
import com.storeops.activities.dto.UpdateActivityRequest;
import com.storeops.activities.model.TaskCategory;
import com.storeops.activities.model.TaskPriority;
import com.storeops.activities.model.TaskStatus;
import com.storeops.activities.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(ActivityController.class)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListActivities() throws Exception {
        final ActivityDto activity = new ActivityDto("id-1", "prog-1", "Fix shelf", "Restocking task",
                TaskStatus.TODO, TaskPriority.HIGH, TaskCategory.RESTOCKING, "user-1", "owner-1", null);
        when(activityService.list(null, null)).thenReturn(Arrays.asList(activity));

        mockMvc.perform(get("/api/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("id-1"))
                .andExpect(jsonPath("$[0].title").value("Fix shelf"));
    }

    @Test
    void testListActivitiesWithFilters() throws Exception {
        final ActivityDto activity = new ActivityDto("id-1", "prog-1", "Fix shelf", "Restocking task",
                TaskStatus.IN_PROGRESS, TaskPriority.HIGH, TaskCategory.RESTOCKING, "user-1", "owner-1", null);
        when(activityService.list("prog-1", TaskStatus.IN_PROGRESS)).thenReturn(Arrays.asList(activity));

        mockMvc.perform(get("/api/activities")
                .param("programme", "prog-1")
                .param("status", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"));
    }

    @Test
    void testCreateActivity() throws Exception {
        final CreateActivityRequest request = new CreateActivityRequest("prog-1", "New task", "Description",
                TaskPriority.MEDIUM, TaskCategory.AUDIT, "user-2", null);
        final ActivityDto created = new ActivityDto("id-2", "prog-1", "New task", "Description",
                TaskStatus.TODO, TaskPriority.MEDIUM, TaskCategory.AUDIT, "user-2", null, null);
        when(activityService.create(any())).thenReturn(created);

        mockMvc.perform(post("/api/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("id-2"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void testGetActivityById() throws Exception {
        final ActivityDto activity = new ActivityDto("id-1", "prog-1", "Fix shelf", "Restocking task",
                TaskStatus.DONE, TaskPriority.HIGH, TaskCategory.RESTOCKING, "user-1", "owner-1", null);
        when(activityService.getById("id-1")).thenReturn(activity);

        mockMvc.perform(get("/api/activities/id-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("id-1"))
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void testUpdateActivity() throws Exception {
        final UpdateActivityRequest request = new UpdateActivityRequest(TaskStatus.IN_PROGRESS, null, null, null, null);
        final ActivityDto updated = new ActivityDto("id-1", "prog-1", "Fix shelf", "Restocking task",
                TaskStatus.IN_PROGRESS, TaskPriority.HIGH, TaskCategory.RESTOCKING, "user-1", "owner-1", null);
        when(activityService.update(anyString(), any())).thenReturn(updated);

        mockMvc.perform(patch("/api/activities/id-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testDeleteActivity() throws Exception {
        mockMvc.perform(delete("/api/activities/id-1")
                .header("X-User-Id", "user-1"))
                .andExpect(status().isNoContent());

        verify(activityService).delete("id-1", "user-1");
    }
}
