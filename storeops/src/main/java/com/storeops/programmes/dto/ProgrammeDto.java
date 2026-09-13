package com.storeops.programmes.dto;

import com.storeops.programmes.model.ProjectMember;
import java.util.List;

public record ProgrammeDto(
        String id,
        String storeId,
        String name,
        String description,
        List<ProjectMember> members
) {
}
