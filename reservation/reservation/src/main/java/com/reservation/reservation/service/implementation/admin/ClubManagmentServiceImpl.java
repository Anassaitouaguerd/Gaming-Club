package com.reservation.reservation.service.implementation.admin;

import com.reservation.reservation.dto.ClubDTO;
import com.reservation.reservation.entity.Club;
import com.reservation.reservation.entity.Resource;
import com.reservation.reservation.mapper.ClubMapper;
import com.reservation.reservation.repository.ClubRepository;
import com.reservation.reservation.repository.ResourceRepository;
import com.reservation.reservation.service.interfaces.admin.ClubManagmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor

public class ClubManagmentServiceImpl implements ClubManagmentService {

    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    private final ResourceRepository resourceRepository;

    @Override
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
    public List<Club> getClubsByCity(String city) {
        return List.of();
    }

    @Override
    public ClubDTO updateClub(Long clubId, ClubDTO club) {
        Club existingClub = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found with id: " + clubId));

        existingClub.setName(club.name());
        existingClub.setCity(club.city());
        existingClub.setAddress(club.address());
        existingClub.setDescription(club.description());
        existingClub.setContactInfo(club.contactInfo());
        existingClub.setUpdatedAt(LocalDateTime.now());

        // Properly handle resources
        if (club.resourceIds() != null) {
            List<Resource> resources = club.resourceIds().stream()
                    .map(resourceId -> resourceRepository.findById(resourceId)
                            .orElseThrow(() -> new IllegalArgumentException("Resource not found with id: " + resourceId)))
                    .toList();

            existingClub.setResources(resources);
        }

        return clubMapper.toClubDTO(clubRepository.save(existingClub));
    }

    @Override
    public void deleteClub(Long clubId) {

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found with id: " + clubId));

        clubRepository.delete(club);
    }
}
