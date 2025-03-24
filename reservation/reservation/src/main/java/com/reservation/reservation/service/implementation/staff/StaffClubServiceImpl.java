package com.reservation.reservation.service.implementation.staff;

import com.reservation.reservation.dto.staff.ClubStaffDTO;
import com.reservation.reservation.dto.staff.ReservationStaffDTO;
import com.reservation.reservation.dto.user.UserDto;
import com.reservation.reservation.entity.Club;
import com.reservation.reservation.entity.Reservation;
import com.reservation.reservation.repository.ClubRepository;
import com.reservation.reservation.repository.ReservationRepository;
import com.reservation.reservation.service.interfaces.staff.StaffClubService;
import com.reservation.reservation.controller.FeignClient.UserServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StaffClubServiceImpl implements StaffClubService {
    private final ClubRepository clubRepository;
    private final UserServiceClient userServiceClient;
    private final ReservationRepository reservationRepository;

    @Override
    public List<ClubStaffDTO> getClubByStaff() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            Long userId = userServiceClient.findByUsername(username)
                    .map(UserDto::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Change here: Get Club entities from repository and convert to DTOs
            List<Club> clubs = clubRepository.findClubsByStaffId(userId);
            return clubs.stream()
                    .map(this::convertToClubDTO)
                    .collect(Collectors.toList());
        }

        throw new RuntimeException("Not authenticated");
    }

    @Override
    public List<ReservationStaffDTO> getReservationsByStaffClubs() {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            // Get the user ID from the User Service
            Long userId = userServiceClient.findByUsername(username)
                    .map(UserDto::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Fetch clubs associated with this user ID
            List<Club> staffClubs = clubRepository.findClubsByStaffId(userId);

            if (staffClubs.isEmpty()) {
                return new ArrayList<>();
            }

            // Get all club IDs
            List<Long> clubIds = staffClubs.stream()
                    .map(Club::getId)
                    .collect(Collectors.toList());

            // Fetch reservations for these clubs
            List<Reservation> reservations = reservationRepository.findByClubIdIn(clubIds);

            // Convert to DTOs
            return reservations.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }

        throw new RuntimeException("Not authenticated");
    }

    @Override
    public List<ReservationStaffDTO> getReservationsByClub(Long clubId) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            // Get the user ID from the User Service
            Long userId = userServiceClient.findByUsername(username)
                    .map(UserDto::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Verify the club belongs to this staff member
            boolean hasAccess = clubRepository.checkClubBelongsToStaff(clubId, userId);
            if (!hasAccess) {
                throw new RuntimeException("Unauthorized access to club");
            }

            // Fetch reservations for this club
            List<Reservation> reservations = reservationRepository.findByClubId(clubId);

            // Convert to DTOs
            return reservations.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }

        throw new RuntimeException("Not authenticated");
    }

    // Helper method to convert Reservation entity to DTO
    private ReservationStaffDTO convertToDTO(Reservation reservation) {
        return ReservationStaffDTO.builder()
                .id(reservation.getId())
                .clubId(reservation.getClub().getId())
                .clubName(reservation.getClub().getName())
                .customerId(reservation.getUserId())
                .startTime(reservation.getStartTime().toString())
                .endTime(reservation.getEndTime().toString())
                .status(String.valueOf(reservation.getStatus()))
                .totalPrice(100.0)
                .createdAt(reservation.getCreatedAt().toString())
                .build();
    }

    // Helper method to convert Club entity to ClubDTO
    private ClubStaffDTO convertToClubDTO(Club club) {
        return new ClubStaffDTO(
                club.getId(),
                club.getName(),
                club.getAddress(),
                club.getCity(),
                club.getDescription(),
                club.getContactInfo(),
                club.getAdmin(),
                club.isApproved(),
                club.getCreatedAt(),
                club.getUpdatedAt()
        );
    }
}