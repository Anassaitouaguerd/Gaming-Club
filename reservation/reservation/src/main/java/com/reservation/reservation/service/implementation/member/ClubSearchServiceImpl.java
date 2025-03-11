package com.reservation.reservation.service.implementation.member;

import com.reservation.reservation.dto.ClubDTO;
import com.reservation.reservation.mapper.ClubMapper;
import com.reservation.reservation.repository.ClubRepository;
import com.reservation.reservation.service.interfaces.member.ClubSearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClubSearchServiceImpl implements ClubSearchService {

    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    @Override
    public List<ClubDTO> findClubsByCity(String cityName) {
        return clubRepository.findByCity(cityName)
        .stream()
        .map(clubMapper::toClubDTO)
        .toList();
    }
}
