package com.storeops.programmes.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.storeops.common.errors.NotFoundError;
import com.storeops.common.events.EventBus;
import com.storeops.programmes.dto.AddMemberRequest;
import com.storeops.programmes.dto.CreateProgrammeRequest;
import com.storeops.programmes.model.Project;
import com.storeops.programmes.model.ProjectRole;
import com.storeops.programmes.repository.ProgrammeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProgrammeServiceTest {

    @Mock
    private ProgrammeRepository programmeRepository;

    @Mock
    private EventBus eventBus;

    private ProgrammeService programmeService;

    @BeforeEach
    void setUp() {
        programmeService = new ProgrammeServiceImpl(programmeRepository, eventBus);
    }

    @Test
    void testListForStoreReturnsEmptyList() {
        when(programmeRepository.findAllByStoreId("store-1")).thenReturn(java.util.List.of());

        final var result = programmeService.listForStore("store-1");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateProgrammeReturnsNewProgramme() {
        final CreateProgrammeRequest request = new CreateProgrammeRequest("store-1", "Q4 Planning", "Desc");
        final Project project = new Project();
        project.setId("prog-1");
        project.setStoreId("store-1");
        project.setName("Q4 Planning");
        when(programmeRepository.save(any())).thenReturn(project);

        final var result = programmeService.create(request);

        assertNotNull(result);
        assertEquals("prog-1", result.id());
        assertEquals("Q4 Planning", result.name());
    }

    @Test
    void testAddMemberThrowsNotFoundWhenProgrammeNotExists() {
        when(programmeRepository.findById("non-existent")).thenReturn(Optional.empty());

        assertThrows(NotFoundError.class, () ->
                programmeService.addMember("non-existent", new AddMemberRequest("user-1", ProjectRole.STORE_MANAGER)));
    }

    @Test
    void testAddMemberToExistingProgramme() {
        final Project project = new Project();
        project.setId("prog-1");
        project.setStoreId("store-1");
        project.setMembers(new java.util.ArrayList<>());
        when(programmeRepository.findById("prog-1")).thenReturn(Optional.of(project));
        when(programmeRepository.save(any())).thenReturn(project);

        final var result = programmeService.addMember("prog-1", new AddMemberRequest("user-1", ProjectRole.STORE_MANAGER));

        assertNotNull(result);
        verify(programmeRepository).save(any());
    }

    @Test
    void testGetByIdReturnsProgrammeWithMembersWhenExists() {
        final Project project = new Project();
        project.setId("prog-1");
        project.setStoreId("store-1");
        project.setName("Q4 Planning");
        when(programmeRepository.findById("prog-1")).thenReturn(Optional.of(project));

        final var result = programmeService.getById("prog-1");

        assertNotNull(result);
        assertEquals("prog-1", result.id());
        assertEquals("Q4 Planning", result.name());
        verify(programmeRepository, never()).save(any());
    }

    @Test
    void testGetByIdThrowsNotFoundErrorWhenProgrammeNotExists() {
        when(programmeRepository.findById("non-existent")).thenReturn(Optional.empty());

        assertThrows(NotFoundError.class, () -> programmeService.getById("non-existent"));
    }

    @Test
    void testCloseEmitsEvent() {
        final Project project = new Project();
        project.setId("prog-1");
        project.setStoreId("store-1");
        when(programmeRepository.findById("prog-1")).thenReturn(Optional.of(project));
        when(programmeRepository.save(any())).thenReturn(project);

        programmeService.close("prog-1");

        verify(eventBus).emit(any());
    }
}
