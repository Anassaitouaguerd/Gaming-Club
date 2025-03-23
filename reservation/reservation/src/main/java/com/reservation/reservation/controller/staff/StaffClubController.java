package com.reservation.reservation.controller.staff;

import com.reservation.reservation.dto.ClubDTO;
import com.reservation.reservation.service.interfaces.staff.StaffClubService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/staff/clubs")
@AllArgsConstructor
public class StaffClubController {

    private final StaffClubService staffClubService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('STAFF')")  // Ensures only users with STAFF role can access this endpoint
    public ResponseEntity<List<ClubDTO>> getStaffClubs() {
        List<ClubDTO> clubs = staffClubService.getClubByStaff();
        return ResponseEntity.ok(clubs);
    }
}