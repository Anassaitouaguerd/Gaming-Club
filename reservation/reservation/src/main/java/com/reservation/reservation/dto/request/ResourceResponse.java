package com.reservation.reservation.dto.request;

import com.reservation.reservation.entity.enums.ResourceStatus;
import lombok.Builder;

@Builder
public record ResourceResponse(
        Long id,
        String name,
        ResourceStatus status
) {}
