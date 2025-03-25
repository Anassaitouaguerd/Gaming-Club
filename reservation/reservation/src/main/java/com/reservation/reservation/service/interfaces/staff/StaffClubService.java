package com.reservation.reservation.service.interfaces.staff;

import com.reservation.reservation.dto.staff.ClubStaffDTO;
import com.reservation.reservation.dto.staff.ReservationStaffDTO;

import java.util.List;

public interface StaffClubService {
    List<ClubStaffDTO> getClubByStaff();
    List<ReservationStaffDTO> getReservationsByStaffClubs();
    List<ReservationStaffDTO> getReservationsByClub(Long clubId);
    ClubStaffDTO createClub(ClubStaffDTO clubDTO);
    ClubStaffDTO updateClub(Long clubId, ClubStaffDTO clubDTO);
    void deleteClub(Long clubId);
}
