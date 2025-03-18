package com.reservation.reservation.mapper;

import com.reservation.reservation.entity.Resource;
import com.reservation.reservation.dto.resource.ResourceDTO;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper {
    public ResourceDTO toDTO(Resource resource) {
        if (resource == null) {
            return null;
        }

        return ResourceDTO.builder()
                .id(resource.getId())
                .name(resource.getName())
                .type(resource.getType())
                .status(resource.getStatus())
                .clubId(resource.getClub() != null ? resource.getClub().getId() : null)
                .build();
    }

    public Resource toEntity(ResourceDTO resourceDTO) {
        if (resourceDTO == null) {
            return null;
        }

        Resource resource = new Resource();
        resource.setId(resourceDTO.id());
        resource.setName(resourceDTO.name());
        resource.setType(resourceDTO.type());
        resource.setStatus(resourceDTO.status());

        return resource;
    }
}