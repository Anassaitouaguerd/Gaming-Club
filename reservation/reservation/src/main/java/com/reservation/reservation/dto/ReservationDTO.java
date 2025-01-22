package com.reservation.reservation.dto;

import com.reservation.reservation.entity.enums.ReservationStatus;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record ReservationDTO(
        Long id,
        Long userId,
        ResourceDTO resource,
        ClubDTO club,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ReservationStatus status,
        String cancellationReason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long staffHandler
) {}
