package com.reservation.reservation.mapper.request;

import com.reservation.reservation.dto.request.ResourceResponse;
import com.reservation.reservation.entity.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MemberResourceMapper {

    public ResourceResponse toResponse(Resource resource) {
        if (resource == null) {
            return null;
        }

        return new ResourceResponse(
                resource.getId(),
                resource.getName(),
                resource.getStatus()
        );
    }

    public List<ResourceResponse> toResponseList(Optional<List<Resource>> resources) {
        if (resources.isEmpty()) {
            return Collections.emptyList();
        }

        return resources.get().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

