package com.reservation.reservation.dto.resource;

import com.reservation.reservation.entity.enums.ResourceStatus;
import com.reservation.reservation.entity.enums.ResourceType;
import lombok.Builder;

@Builder
public record ResourceDTO(
        Long id,
        String name,
        ResourceType type,
        ResourceStatus status,
        Long clubId
) {}
