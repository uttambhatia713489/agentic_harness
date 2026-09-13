package com.storeops.programmes.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeops.programmes.dto.AddMemberRequest;
import com.storeops.programmes.dto.CreateProgrammeRequest;
import com.storeops.programmes.dto.ProgrammeDto;
import com.storeops.programmes.model.ProjectRole;
import com.storeops.programmes.service.ProgrammeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Arrays;

@WebMvcTest(ProgrammeController.class)
class ProgrammeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgrammeService programmeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListProgrammesForStore() throws Exception {
        final ProgrammeDto programme = new ProgrammeDto("prog-1", "store-1", "Q4 Inventory", "Quarterly inventory",
                new ArrayList<>());
        when(programmeService.listForStore("store-1")).thenReturn(Arrays.asList(programme));

        mockMvc.perform(get("/api/programmes")
                .header("X-Store-Id", "store-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("prog-1"))
                .andExpect(jsonPath("$[0].name").value("Q4 Inventory"));
    }

    @Test
    void testCreateProgramme() throws Exception {
        final CreateProgrammeRequest request = new CreateProgrammeRequest("store-1", "New Programme",
                "Description");
        final ProgrammeDto created = new ProgrammeDto("prog-2", "store-1", "New Programme", "Description",
                new ArrayList<>());
        when(programmeService.create(any())).thenReturn(created);

        mockMvc.perform(post("/api/programmes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("prog-2"))
                .andExpect(jsonPath("$.storeId").value("store-1"));
    }

    @Test
    void testAddMemberToProgramme() throws Exception {
        final AddMemberRequest request = new AddMemberRequest("user-1", ProjectRole.DEPARTMENT_LEAD);
        final ProgrammeDto updated = new ProgrammeDto("prog-1", "store-1", "Q4 Inventory", "Quarterly inventory",
                new ArrayList<>());
        when(programmeService.addMember(anyString(), any())).thenReturn(updated);

        mockMvc.perform(post("/api/programmes/prog-1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("prog-1"))
                .andExpect(jsonPath("$.name").value("Q4 Inventory"));
    }
}
