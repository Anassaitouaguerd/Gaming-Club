package com.reservation.reservation.controller.member;

import com.reservation.reservation.dto.request.ReservationRequest;
import com.reservation.reservation.dto.request.ReservationResponse;
import com.reservation.reservation.dto.request.ResourceResponse;
import com.reservation.reservation.entity.Resource;
import com.reservation.reservation.service.interfaces.member.ReservationMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member/reservation")
@RequiredArgsConstructor
public class MemberReservationController {

    private final ReservationMemberService reservationService;

    @GetMapping("/resources/available")
    public ResponseEntity<List<ResourceResponse>> getAvailableResources() {
        List<ResourceResponse> resources = reservationService.getAvailableResources();
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<ReservationResponse> createReservation(
            @PathVariable Long userId,
            @Valid @RequestBody ReservationRequest request) {
        ReservationResponse reservation = reservationService.makeReservation(request , userId);

        return new ResponseEntity<>(mapToReservationResponse(reservation), HttpStatus.CREATED);
    }

    @DeleteMapping("/cancel/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{reservationId}")
    public ResponseEntity<ReservationResponse> modifyReservation(
            @PathVariable Long reservationId,
            @Valid @RequestBody ReservationRequest request) {

        ReservationResponse updatedReservation = reservationService.modifyReservation(
                reservationId, request
        );

        return ResponseEntity.ok(mapToReservationResponse(updatedReservation));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponse>> getUserReservations(
            @PathVariable Long userId) {

        List<ReservationResponse> reservations = reservationService.getUserReservations(
                userId
        );

        List<ReservationResponse> response = reservations.stream()
                .map(this::mapToReservationResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    private ReservationResponse mapToReservationResponse(ReservationResponse reservation) {
        return ReservationResponse.builder()
                .id(reservation.id())
                .resourceId(reservation.resourceId())
                .resourceName(reservation.resourceName())
                .startTime(reservation.startTime())
                .endTime(reservation.endTime())
                .status(reservation.status())
                .createdAt(reservation.createdAt())
                .modifiedAt(reservation.modifiedAt())
                .build();
    }
}