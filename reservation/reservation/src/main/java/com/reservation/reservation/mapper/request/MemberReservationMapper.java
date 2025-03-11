package com.reservation.reservation.mapper.request;

import com.reservation.reservation.dto.request.ReservationRequest;
import com.reservation.reservation.dto.request.ReservationResponse;
import com.reservation.reservation.entity.Reservation;
import com.reservation.reservation.entity.Resource;
import com.reservation.reservation.entity.enums.ReservationStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MemberReservationMapper {

    public Reservation toEntity(ReservationRequest request, Long memberId, Resource resource) {
        if (request == null) {
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(memberId);
        reservation.setResource(resource);
        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.endTime());
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setCreatedAt(LocalDateTime.now());

        return reservation;
    }

    public ReservationResponse toResponse(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        return new ReservationResponse(
                reservation.getId(),
                Optional.ofNullable(reservation.getResource())
                        .map(Resource::getId)
                        .orElse(null),
                Optional.ofNullable(reservation.getResource())
                        .map(Resource::getName)
                        .orElse(null),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }

    public List<ReservationResponse> toResponseList(List<Reservation> reservations) {
        if (reservations == null) {
            return Collections.emptyList();
        }

        return reservations.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
