package com.reservation.reservation.mapper;

import com.reservation.reservation.dto.ClubDTO;
import com.reservation.reservation.entity.Club;
import com.reservation.reservation.entity.Resource;
import com.reservation.reservation.repository.ResourceRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ClubMapper {

    private final ResourceRepository resourceRepository;

    public ClubMapper(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public ClubDTO toClubDTO(Club club) {
        if (club == null) return null;
        return ClubDTO.builder()
                .id(club.getId())
                .name(club.getName())
                .city(club.getCity())
                .contactInfo(club.getContactInfo())
                .address(club.getAddress())
                .description(club.getDescription())
                .resourceIds(club.getResources() != null
                        ? club.getResources().stream()
                        .map(Resource::getId)
                        .toList()
                        : null)
                .build();
    }

    public Club toEntity(ClubDTO clubDTO) {
        if (clubDTO == null) return null;

        Club club = new Club();
        club.setId(clubDTO.id());
        club.setName(clubDTO.name());
        club.setCity(clubDTO.city());
        club.setContactInfo(clubDTO.contactInfo());
        club.setAddress(clubDTO.address());
        club.setDescription(clubDTO.description());

        club.setApproved(false);
        club.setCreatedAt(LocalDateTime.now());
        club.setUpdatedAt(LocalDateTime.now());

        // Check and validate resources
        if (clubDTO.resourceIds() != null) {
            List<Resource> resources = clubDTO.resourceIds().stream()
                    .map(resourceId -> resourceRepository.findById(resourceId)
                            .orElseThrow(() -> new IllegalArgumentException("Resource not found with id: " + resourceId)))
                    .toList();

            club.setResources(resources);
            resources.forEach(resource -> resource.setClub(club));
        }

        return club;
    }
}