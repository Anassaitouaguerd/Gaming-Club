package com.reservation.reservation.dto.aboutReservations;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AboutReservationsDTO(
        Long id,
        String experienceLevel,
        String preferredPlatform,
        String bookingDate,
        Integer duration,
        Integer stationNumber,
        String additionalRequests,
        List<String> gamePreferences
) {
}
