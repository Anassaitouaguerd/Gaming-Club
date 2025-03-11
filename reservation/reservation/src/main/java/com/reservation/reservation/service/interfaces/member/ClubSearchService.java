package com.reservation.reservation.service.interfaces.member;

import com.reservation.reservation.dto.ClubDTO;

import java.util.List;

public interface ClubSearchService {

    List<ClubDTO> findClubsByCity(String cityName);
}
