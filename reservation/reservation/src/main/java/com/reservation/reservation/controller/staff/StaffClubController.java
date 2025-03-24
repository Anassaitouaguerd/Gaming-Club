package com.reservation.reservation.controller.staff;

import com.reservation.reservation.dto.staff.ClubStaffDTO;
import com.reservation.reservation.dto.staff.ReservationStaffDTO;
import com.reservation.reservation.service.interfaces.staff.StaffClubService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/staff/clubs")
@AllArgsConstructor
public class StaffClubController {

    private final StaffClubService staffClubService;

    @GetMapping("/all")
    public ResponseEntity<List<ClubStaffDTO>> getStaffClubs() {
        List<ClubStaffDTO> clubs = staffClubService.getClubByStaff();
        return ResponseEntity.ok(clubs);
    }

    @GetMapping("/all/reservations")
    public ResponseEntity<List<ReservationStaffDTO>> getStaffReservations() {
        List<ReservationStaffDTO> reservations = staffClubService.getReservationsByStaffClubs();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<ReservationStaffDTO>> getReservationsByClub(@PathVariable Long clubId) {
        List<ReservationStaffDTO> reservations = staffClubService.getReservationsByClub(clubId);
        return ResponseEntity.ok(reservations);
    }
}