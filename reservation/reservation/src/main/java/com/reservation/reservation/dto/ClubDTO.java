package com.reservation.reservation.dto;

import com.reservation.reservation.dto.resource.ResourceResponseDTO;
import com.reservation.reservation.entity.Resource;
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
        List<ResourceResponseDTO> resources
) {}
