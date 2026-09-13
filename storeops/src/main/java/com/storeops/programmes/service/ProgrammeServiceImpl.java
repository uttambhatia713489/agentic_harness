package com.storeops.programmes.service;

import com.storeops.common.errors.NotFoundError;
import com.storeops.common.events.EventBus;
import com.storeops.programmes.dto.AddMemberRequest;
import com.storeops.programmes.dto.CreateProgrammeRequest;
import com.storeops.programmes.dto.ProgrammeDto;
import com.storeops.programmes.events.ProgrammeClosedEvent;
import com.storeops.programmes.model.Project;
import com.storeops.programmes.model.ProjectMember;
import com.storeops.programmes.repository.ProgrammeRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ProgrammeServiceImpl implements ProgrammeService {

    private final ProgrammeRepository programmeRepository;
    private final EventBus eventBus;

    public ProgrammeServiceImpl(final ProgrammeRepository programmeRepository, final EventBus eventBus) {
        this.programmeRepository = programmeRepository;
        this.eventBus = eventBus;
    }

    @Override
    public List<ProgrammeDto> listForStore(final String storeId) {
        return programmeRepository.findAllByStoreId(storeId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ProgrammeDto create(final CreateProgrammeRequest request) {
        final Project project = new Project();
        project.setId(UUID.randomUUID().toString());
        project.setStoreId(request.storeId());
        project.setName(request.name());
        project.setDescription(request.description());
        project.setCreatedAt(Instant.now());
        return toDto(programmeRepository.save(project));
    }

    @Override
    public ProgrammeDto addMember(final String id, final AddMemberRequest request) {
        final Project project = programmeRepository.findById(id)
                .orElseThrow(() -> new NotFoundError("Programme not found: " + id));
        project.getMembers().add(new ProjectMember(request.userId(), request.role()));
        return toDto(programmeRepository.save(project));
    }

    @Override
    public ProgrammeDto close(final String id) {
        final Project project = programmeRepository.findById(id)
                .orElseThrow(() -> new NotFoundError("Programme not found: " + id));
        project.setClosedAt(Instant.now());
        final Project saved = programmeRepository.save(project);
        eventBus.emit(new ProgrammeClosedEvent(saved.getId(), saved.getStoreId()));
        return toDto(saved);
    }

    private ProgrammeDto toDto(final Project project) {
        return new ProgrammeDto(
                project.getId(),
                project.getStoreId(),
                project.getName(),
                project.getDescription(),
                project.getMembers()
        );
    }
}
