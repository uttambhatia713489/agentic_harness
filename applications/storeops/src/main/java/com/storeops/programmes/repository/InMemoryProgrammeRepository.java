package com.storeops.programmes.repository;

import com.storeops.programmes.model.Project;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryProgrammeRepository implements ProgrammeRepository {

    private final Map<String, Project> store = new ConcurrentHashMap<>();

    @Override
    public List<Project> findAllByStoreId(final String storeId) {
        return store.values().stream()
                .filter(project -> storeId == null || storeId.equals(project.getStoreId()))
                .toList();
    }

    @Override
    public Optional<Project> findById(final String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Project save(final Project project) {
        store.put(project.getId(), project);
        return project;
    }
}
