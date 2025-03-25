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

import java.time.LocalDateTime;
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

            List<Club> clubs = clubRepository.findClubsByStaffId(userId);
            return clubs.stream()
                    .map(this::convertToClubDTO)
                    .collect(Collectors.toList());
        }

        throw new RuntimeException("Not authenticated");
    }

    @Override
    public List<ReservationStaffDTO> getReservationsByStaffClubs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            Long userId = userServiceClient.findByUsername(username)
                    .map(UserDto::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Club> staffClubs = clubRepository.findClubsByStaffId(userId);

            if (staffClubs.isEmpty()) {
                return new ArrayList<>();
            }

            List<Long> clubIds = staffClubs.stream()
                    .map(Club::getId)
                    .collect(Collectors.toList());

            List<Reservation> reservations = reservationRepository.findByClubIdIn(clubIds);

            return reservations.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }

        throw new RuntimeException("Not authenticated");
    }

    @Override
    public List<ReservationStaffDTO> getReservationsByClub(Long clubId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            Long userId = userServiceClient.findByUsername(username)
                    .map(UserDto::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            boolean hasAccess = clubRepository.checkClubBelongsToStaff(clubId, userId);
            if (!hasAccess) {
                throw new RuntimeException("Unauthorized access to club");
            }

            List<Reservation> reservations = reservationRepository.findByClubId(clubId);

            return reservations.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }

        throw new RuntimeException("Not authenticated");
    }

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

    @Override
    public ClubStaffDTO createClub(ClubStaffDTO clubDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            Long userId = userServiceClient.findByUsername(username)
                    .map(UserDto::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Club club = new Club();
            club.setName(clubDTO.name());
            club.setAddress(clubDTO.address());
            club.setCity(clubDTO.city());
            club.setDescription(clubDTO.description());
            club.setContactInfo(clubDTO.contactInfo());
            club.setAdmin(userId);
            club.setApproved(false);
            club.setCreatedAt(LocalDateTime.now());
            club.setUpdatedAt(LocalDateTime.now());

            Club savedClub = clubRepository.save(club);

            return convertToClubDTO(savedClub);
        }

        throw new RuntimeException("Not authenticated");
    }

    @Override
    public ClubStaffDTO updateClub(Long clubId, ClubStaffDTO clubDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            Long userId = userServiceClient.findByUsername(username)
                    .map(UserDto::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Club club = clubRepository.findById(clubId)
                    .orElseThrow(() -> new RuntimeException("Club not found"));

            boolean hasAccess = club.getAdmin().equals(userId) ||
                    clubRepository.checkClubBelongsToStaff(clubId, userId);

            if (!hasAccess) {
                throw new RuntimeException("Unauthorized access to club");
            }

            club.setName(clubDTO.name());
            club.setAddress(clubDTO.address());
            club.setCity(clubDTO.city());
            club.setDescription(clubDTO.description());
            club.setContactInfo(clubDTO.contactInfo());
            club.setUpdatedAt(LocalDateTime.now());

            Club updatedClub = clubRepository.save(club);

            return convertToClubDTO(updatedClub);
        }

        throw new RuntimeException("Not authenticated");
    }

    @Override
    public void deleteClub(Long clubId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            Long userId = userServiceClient.findByUsername(username)
                    .map(UserDto::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Club club = clubRepository.findById(clubId)
                    .orElseThrow(() -> new RuntimeException("Club not found"));

            if (!club.getAdmin().equals(userId)) {
                throw new RuntimeException("Only the club admin can delete the club");
            }

            List<Reservation> activeReservations = reservationRepository.findActiveReservationsForClub(clubId);
            if (!activeReservations.isEmpty()) {
                throw new RuntimeException("Cannot delete club with active reservations");
            }
            
            clubRepository.deleteById(clubId);
        } else {
            throw new RuntimeException("Not authenticated");
        }
    }
}