package com.reservation.reservation.service.interfaces.admin;

import com.reservation.reservation.dto.ResourceDTO;
import com.reservation.reservation.entity.enums.ResourceStatus;
import com.reservation.reservation.entity.enums.ResourceType;

import java.util.List;

public interface ResourceManagmentService {

    ResourceDTO createResource(ResourceDTO resourceDTO);
    ResourceDTO updateResource(Long resourceId , ResourceDTO resourceDTO);
    List<ResourceDTO> getAllResources();
    void deleteResource(Long resourceId);

    ResourceDTO getResourceById(Long resourceId);
    List<ResourceDTO> getResourceByStatus(ResourceStatus resourceStatus);
    List<ResourceDTO> getResourceByType(ResourceType resourceType);
    List<ResourceDTO> getResourcesByClubId(Long clubId);
}
