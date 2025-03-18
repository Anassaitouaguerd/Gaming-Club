package com.reservation.reservation.mapper;

import com.reservation.reservation.dto.ClubDTO;
import com.reservation.reservation.dto.resource.ResourceResponseDTO;
import com.reservation.reservation.entity.Club;
import com.reservation.reservation.entity.Resource;
import com.reservation.reservation.repository.ResourceRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
                .resources(club.getResources() != null
                        ? club.getResources().stream()
                        .map(this::toResourceResponseDTO)
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

        // Handle resources
        if (clubDTO.resources() != null && !clubDTO.resources().isEmpty()) {
            List<Resource> resources = new ArrayList<>();

            for (ResourceResponseDTO resourceDTO : clubDTO.resources()) {
                if (resourceDTO.id() != null) {
                    // For existing resources, fetch from repository
                    Resource resource = resourceRepository.findById(resourceDTO.id())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Resource not found with id: " + resourceDTO.id()));

                    // Update resource properties if needed
                    updateResourceFromDTO(resource, resourceDTO);
                    resource.setClub(club);
                    resources.add(resource);
                } else {
                    // For new resources
                    Resource newResource = new Resource();
                    updateResourceFromDTO(newResource, resourceDTO);
                    newResource.setClub(club);
                    resources.add(newResource);
                }
            }

            club.setResources(resources);
        } else {
            club.setResources(new ArrayList<>());
        }

        return club;
    }

    private void updateResourceFromDTO(Resource resource, ResourceResponseDTO resourceDTO) {
        // Copy properties from resourceDTO to resource
        if (resourceDTO.name() != null) {
            resource.setName(resourceDTO.name());
        }
        if (resourceDTO.type() != null) {
            resource.setType(resourceDTO.type());
        }
        if (resourceDTO.status() != null) {
            resource.setStatus(resourceDTO.status());
        }
        // Add other properties as needed
    }

    private ResourceResponseDTO toResourceResponseDTO(Resource resource) {
        if (resource == null) return null;
        return ResourceResponseDTO.builder()
                .id(resource.getId())
                .name(resource.getName())
                .type(resource.getType())
                .status(resource.getStatus())
                .build();
    }
}