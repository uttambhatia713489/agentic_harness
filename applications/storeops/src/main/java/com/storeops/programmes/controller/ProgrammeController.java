package com.storeops.programmes.controller;

import com.storeops.programmes.dto.AddMemberRequest;
import com.storeops.programmes.dto.CreateProgrammeRequest;
import com.storeops.programmes.dto.ProgrammeDto;
import com.storeops.programmes.service.ProgrammeService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/programmes")
public class ProgrammeController {

    private final ProgrammeService programmeService;

    public ProgrammeController(final ProgrammeService programmeService) {
        this.programmeService = programmeService;
    }

    @GetMapping
    public ResponseEntity<List<ProgrammeDto>> list(@RequestHeader(name = "X-Store-Id") final String storeId) {
        return ResponseEntity.ok(programmeService.listForStore(storeId));
    }

    @PostMapping
    public ResponseEntity<ProgrammeDto> create(@Valid @RequestBody final CreateProgrammeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(programmeService.create(request));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<ProgrammeDto> addMember(
            @PathVariable final String id,
            @Valid @RequestBody final AddMemberRequest request) {
        return ResponseEntity.ok(programmeService.addMember(id, request));
    }
}
