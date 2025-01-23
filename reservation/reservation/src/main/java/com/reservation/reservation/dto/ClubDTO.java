package com.reservation.reservation.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ClubDTO(
        Long id,
        String name,
        String city,
        String address,
        String description,
        String contactInfo,
        List<Long> resourceIds
) {}
