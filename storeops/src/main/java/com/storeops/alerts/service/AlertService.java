package com.storeops.alerts.service;

import com.storeops.alerts.dto.NotificationDto;
import java.util.List;

public interface AlertService {

    List<NotificationDto> listForUser(String userId);
}
