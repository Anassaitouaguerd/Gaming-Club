package com.reservation.reservation.service.implementation.staff;

import com.reservation.reservation.dto.ClubDTO;
import com.reservation.reservation.dto.user.UserDto;
import com.reservation.reservation.repository.ClubRepository;
import com.reservation.reservation.service.interfaces.staff.StaffClubService;
import com.reservation.reservation.controller.FeignClient.UserServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StaffClubServiceImpl implements StaffClubService {
    private final ClubRepository clubRepository;
    private final UserServiceClient userServiceClient;

    @Override
    public List<ClubDTO> getClubByStaff() {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            // Get the user ID from the User Service
            Long userId = userServiceClient.findByUsername(username)
                    .map(UserDto::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Fetch clubs associated with this user ID
            return clubRepository.findClubsByStaffId(userId);
        }

        throw new RuntimeException("Not authenticated");
    }
}