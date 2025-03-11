package com.reservation.reservation.service.interfaces.member;

import com.reservation.reservation.dto.request.ReservationRequest;
import com.reservation.reservation.dto.request.ReservationResponse;
import com.reservation.reservation.dto.request.ResourceResponse;
import com.reservation.reservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationMemberService {

    List<ResourceResponse> getAvailableResources();

    ReservationResponse makeReservation(ReservationRequest request, Long userId);

    ReservationResponse cancelReservation(Long reservationId);

    ReservationResponse modifyReservation(Long reservationId, ReservationRequest request);

    List<ReservationResponse> getUserReservations(Long userId);
}
