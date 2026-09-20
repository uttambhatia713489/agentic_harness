package com.storeops.activities.service;

import com.storeops.activities.model.Task;
import java.util.List;

public interface SlaBreachEvaluationService {

    boolean isBreach(Task task);

    List<Task> detectBreaches();
}
