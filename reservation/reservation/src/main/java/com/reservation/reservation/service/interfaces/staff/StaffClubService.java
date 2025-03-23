package com.reservation.reservation.service.interfaces.staff;

import com.reservation.reservation.dto.ClubDTO;

import java.util.List;

public interface StaffClubService {
    List<ClubDTO> getClubByStaff();
}
