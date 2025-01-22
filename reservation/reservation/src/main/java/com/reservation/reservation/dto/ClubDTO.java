package com.reservation.reservation.dto;

import lombok.Builder;

@Builder
public record ClubDTO(
        Long id,
        String name,
        String city,
        String contactInfo
) {}
