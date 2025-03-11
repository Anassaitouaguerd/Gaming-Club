package com.reservation.reservation.dto.request;

import com.reservation.reservation.entity.enums.ReservationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservationResponse(
        Long id,
        Long resourceId,
        String resourceName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ReservationStatus status,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {}
