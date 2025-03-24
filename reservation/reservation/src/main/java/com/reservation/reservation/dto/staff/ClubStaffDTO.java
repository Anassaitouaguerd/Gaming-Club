package com.reservation.reservation.dto.staff;

import java.time.LocalDateTime;
import java.util.List;

public record ClubStaffDTO(
        Long id,
        String name,
        String address,
        String city,
        String description,
        String contactInfo,
        Long admin,
        boolean isApproved,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}