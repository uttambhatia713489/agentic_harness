package com.storeops.activities.service;

import com.storeops.activities.dto.ActivityDto;
import com.storeops.activities.dto.CreateActivityRequest;
import com.storeops.activities.dto.UpdateActivityRequest;
import com.storeops.activities.model.TaskStatus;
import java.util.List;

public interface ActivityService {

    List<ActivityDto> list(String programmeId, TaskStatus status);

    ActivityDto create(CreateActivityRequest request);

    ActivityDto getById(String id);

    ActivityDto update(String id, UpdateActivityRequest request);

    void delete(String id, String requesterId);
}
