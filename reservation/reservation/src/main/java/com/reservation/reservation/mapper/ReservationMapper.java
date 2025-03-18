package com.reservation.reservation.mapper;

import com.reservation.reservation.dto.ReservationDTO;
import com.reservation.reservation.dto.resource.ResourceDTO;
import com.reservation.reservation.entity.Reservation;
import com.reservation.reservation.entity.Resource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ReservationMapper {
    private final ClubMapper clubeMapper;

    public ReservationDTO toDTO(Reservation reservation) {

        return ReservationDTO.builder()
                .id(reservation.getId())
                .userId(reservation.getUserId())
                .resource(toResourceDTO(reservation.getResource()))
                .club(clubeMapper.toClubDTO(reservation.getClub()))
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .status(reservation.getStatus())
                .cancellationReason(reservation.getCancellationReason())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .staffHandler(reservation.getStaffHandler())
                .build();
    }

    public ReservationDTO toDTO(Optional<Reservation> reservation) {
        return ReservationDTO.builder()
                .id(reservation.map(Reservation::getId).orElse(null))
                .userId(reservation.map(Reservation::getUserId).orElse(null))
                .resource(reservation.map(Reservation::getResource).map(this::toResourceDTO).orElse(null))
                .club(reservation.map(Reservation::getClub).map(clubeMapper::toClubDTO).orElse(null))
                .startTime(reservation.map(Reservation::getStartTime).orElse(null))
                .endTime(reservation.map(Reservation::getEndTime).orElse(null))
                .status(reservation.map(Reservation::getStatus).orElse(null))
                .cancellationReason(reservation.map(Reservation::getCancellationReason).orElse(null))
                .createdAt(reservation.map(Reservation::getCreatedAt).orElse(null))
                .updatedAt(reservation.map(Reservation::getUpdatedAt).orElse(null))
                .staffHandler(reservation.map(Reservation::getStaffHandler).orElse(null))
                .build();
    }

    private ResourceDTO toResourceDTO(Resource resource) {
        if (resource == null) return null;
        return ResourceDTO.builder()
                .id(resource.getId())
                .name(resource.getName())
                .type(resource.getType())
                .status(resource.getStatus())
                .build();
    }


}