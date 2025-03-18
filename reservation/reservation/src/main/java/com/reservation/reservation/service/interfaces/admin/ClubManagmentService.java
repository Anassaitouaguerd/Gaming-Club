package com.reservation.reservation.service.interfaces.admin;

import com.reservation.reservation.dto.ClubDTO;
import com.reservation.reservation.entity.Club;

import java.util.List;

public interface ClubManagmentService {
    // Create operations
    ClubDTO createClub(ClubDTO club);

    // Read operations
    ClubDTO getClubById(Long clubId);
    List<ClubDTO> getAllClubs();
    List<ClubDTO> getClubsByCity(String city);

    // Update operations
    ClubDTO updateClub(Long clubId, ClubDTO club);

    // Delete operations
    void deleteClub(Long clubId);

}
