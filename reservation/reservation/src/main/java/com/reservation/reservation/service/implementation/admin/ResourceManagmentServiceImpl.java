package com.reservation.reservation.service.implementation.admin;

import com.reservation.reservation.dto.ResourceDTO;
import com.reservation.reservation.entity.Club;
import com.reservation.reservation.entity.Resource;
import com.reservation.reservation.entity.enums.ResourceStatus;
import com.reservation.reservation.entity.enums.ResourceType;
import com.reservation.reservation.mapper.ResourceMapper;
import com.reservation.reservation.repository.ClubRepository;
import com.reservation.reservation.repository.ResourceRepository;
import com.reservation.reservation.service.interfaces.admin.ResourceManagmentService;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResourceManagmentServiceImpl implements ResourceManagmentService {

    private final ResourceRepository resourceRepository;
    private final ClubRepository clubRepository;
    private final ResourceMapper resourceMapper;
    private static final String RESOURCE_NOT_FOUND = "Resource not found with id: ";
    private static final String CLUB_NOT_FOUND = "Resource not found with id: ";

    @Override
    @Transactional
    public ResourceDTO createResource(ResourceDTO resourceDTO) {
        validateResourceDTO(resourceDTO);

        Club existingClub = clubRepository.findById(resourceDTO.clubId())
                .orElseThrow(() -> new EntityNotFoundException(CLUB_NOT_FOUND + resourceDTO.clubId()));

        if (resourceRepository.existsByNameAndClub(resourceDTO.name(), existingClub)) {
            throw new DataIntegrityViolationException("Resource with name " + resourceDTO.name() + " already exists in this club");
        }

        Resource resource = resourceMapper.toEntity(resourceDTO);
        resource.setClub(existingClub);
        resource.setStatus(Optional.ofNullable(resource.getStatus()).orElse(ResourceStatus.AVAILABLE));

        return resourceMapper.toDTO(resourceRepository.save(resource));
    }

    @Override
    @Transactional
    public ResourceDTO updateResource(Long resourceId, ResourceDTO resourceDTO) {
        Resource existingResource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new EntityNotFoundException(RESOURCE_NOT_FOUND + resourceId));

        validateResourceDTO(resourceDTO);

        Club existingClub = clubRepository.findById(resourceDTO.clubId())
                .orElseThrow(() -> new EntityNotFoundException(CLUB_NOT_FOUND + resourceDTO.clubId()));

        if (resourceRepository.existsByNameAndClub(resourceDTO.name(), existingClub)) {
            throw new DataIntegrityViolationException("Resource with name " + resourceDTO.name() + " already exists in this club");
        }

        existingResource.setName(resourceDTO.name());
        existingResource.setType(resourceDTO.type());
        existingResource.setStatus(resourceDTO.status());
        existingResource.setClub(existingClub);

        return resourceMapper.toDTO(resourceRepository.save(existingResource));
    }

    @Override
    public List<ResourceDTO> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(resourceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteResource(Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new EntityNotFoundException(RESOURCE_NOT_FOUND + resourceId));
        resourceRepository.delete(resource);
    }

    @Override
    public ResourceDTO getResourceById(Long resourceId) {
        return resourceMapper.toDTO(
                resourceRepository.findById(resourceId)
                        .orElseThrow(() -> new EntityNotFoundException(RESOURCE_NOT_FOUND + resourceId))
        );
    }

    @Override
    public List<ResourceDTO> getResourceByStatus(ResourceStatus resourceStatus) {
        return resourceRepository.findByStatus(resourceStatus).stream()
                .map(resource -> resourceMapper.toDTO((Resource) resource))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceDTO> getResourceByType(ResourceType resourceType) {
        return resourceRepository.findByType(resourceType).stream()
                .map(resource -> resourceMapper.toDTO((Resource) resource))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceDTO> getResourcesByClubId(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException(CLUB_NOT_FOUND+ clubId));
        return resourceRepository.findByClub(club).stream()
                .map(resource -> resourceMapper.toDTO((Resource) resource))
                .collect(Collectors.toList());
    }

    private void validateResourceDTO(ResourceDTO resourceDTO) {
        Optional.ofNullable(resourceDTO)
                .filter(dto -> !StringUtils.isBlank(dto.name()))
                .filter(dto -> dto.type() != null)
                .filter(dto -> dto.clubId() != null)
                .orElseThrow(() -> new IllegalArgumentException("Invalid resource data"));
    }
}