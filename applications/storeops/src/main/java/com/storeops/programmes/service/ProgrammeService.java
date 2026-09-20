package com.storeops.programmes.service;

import com.storeops.programmes.dto.AddMemberRequest;
import com.storeops.programmes.dto.CreateProgrammeRequest;
import com.storeops.programmes.dto.ProgrammeDto;
import java.util.List;

public interface ProgrammeService {

    List<ProgrammeDto> listForStore(String storeId);

    ProgrammeDto getById(String id);

    ProgrammeDto create(CreateProgrammeRequest request);

    ProgrammeDto addMember(String id, AddMemberRequest request);

    ProgrammeDto close(String id);
}
