package com.storeops.activities.controller;

import com.storeops.activities.dto.ActivityDto;
import com.storeops.activities.dto.CreateActivityRequest;
import com.storeops.activities.dto.UpdateActivityRequest;
import com.storeops.activities.model.TaskStatus;
import com.storeops.activities.service.ActivityService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(final ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<List<ActivityDto>> list(
            @RequestParam(name = "programme", required = false) final String programmeId,
            @RequestParam(name = "status", required = false) final TaskStatus status) {
        return ResponseEntity.ok(activityService.list(programmeId, status));
    }

    @PostMapping
    public ResponseEntity<ActivityDto> create(@Valid @RequestBody final CreateActivityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDto> getById(@PathVariable final String id) {
        return ResponseEntity.ok(activityService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ActivityDto> update(
            @PathVariable final String id,
            @RequestBody final UpdateActivityRequest request) {
        return ResponseEntity.ok(activityService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable final String id,
            @RequestHeader(name = "X-User-Id") final String requesterId) {
        activityService.delete(id, requesterId);
        return ResponseEntity.noContent().build();
    }
}
