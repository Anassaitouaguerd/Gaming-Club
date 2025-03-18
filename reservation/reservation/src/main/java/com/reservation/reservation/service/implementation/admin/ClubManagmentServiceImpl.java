package com.reservation.reservation.service.implementation.admin;

import com.reservation.reservation.dto.ClubDTO;
import com.reservation.reservation.dto.resource.ResourceResponseDTO;
import com.reservation.reservation.entity.Club;
import com.reservation.reservation.entity.Resource;
import com.reservation.reservation.mapper.ClubMapper;
import com.reservation.reservation.repository.ClubRepository;
import com.reservation.reservation.repository.ResourceRepository;
import com.reservation.reservation.service.interfaces.admin.ClubManagmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ClubManagmentServiceImpl implements ClubManagmentService {

    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    private final ResourceRepository resourceRepository;

    @Override
    @Transactional
    public ClubDTO createClub(ClubDTO club) {
        return clubMapper.toClubDTO(clubRepository.save(clubMapper.toEntity(club)));
    }

    @Override
    public ClubDTO getClubById(Long clubId) {
        return clubRepository.findById(clubId)
                .map(clubMapper::toClubDTO)
                .orElse(null);
    }

    @Override
    public List<ClubDTO> getAllClubs() {
        return clubRepository.findAll().stream()
                .map(clubMapper::toClubDTO)
                .toList();
    }

    @Override
    public List<ClubDTO> getClubsByCity(String city) {
        return clubRepository.findByCity(city).stream()
                .map(clubMapper::toClubDTO)
                .toList();
    }

    @Override
    @Transactional
    public ClubDTO updateClub(Long clubId, ClubDTO clubDTO) {
        Club existingClub = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found with id: " + clubId));

        // Update basic club properties
        existingClub.setName(clubDTO.name());
        existingClub.setCity(clubDTO.city());
        existingClub.setAddress(clubDTO.address());
        existingClub.setDescription(clubDTO.description());
        existingClub.setContactInfo(clubDTO.contactInfo());

        // Handle resources - leverage the mapper's toEntity method for resource handling
        if (clubDTO.resources() != null) {
            // Create a temporary club with the updated resources
            Club tempClub = clubMapper.toEntity(clubDTO);

            // Clear existing resources and set the new ones
            existingClub.getResources().clear();

            // Get the IDs of resources in the DTO
            List<Long> resourceIds = clubDTO.resources().stream()
                    .map(ResourceResponseDTO::id)
                    .filter(id -> id != null)
                    .toList();

            // Fetch existing resources from repository
            List<Resource> resources = resourceIds.isEmpty()
                    ? List.of()
                    : resourceRepository.findAllById(resourceIds);

            // Update existing resources with new properties
            for (Resource resource : resources) {
                ResourceResponseDTO matchingDTO = clubDTO.resources().stream()
                        .filter(dto -> dto.id() != null && dto.id().equals(resource.getId()))
                        .findFirst()
                        .orElse(null);

                if (matchingDTO != null) {
                    if (matchingDTO.name() != null) resource.setName(matchingDTO.name());
                    if (matchingDTO.type() != null) resource.setType(matchingDTO.type());
                    if (matchingDTO.status() != null) resource.setStatus(matchingDTO.status());
                }

                resource.setClub(existingClub);
                existingClub.getResources().add(resource);
            }
        }

        return clubMapper.toClubDTO(clubRepository.save(existingClub));
    }

    @Override
    @Transactional
    public void deleteClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found with id: " + clubId));

        clubRepository.delete(club);
    }
}