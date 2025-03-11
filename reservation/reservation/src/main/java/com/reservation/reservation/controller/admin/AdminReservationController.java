package com.reservation.reservation.controller.admin;

import com.reservation.reservation.dto.ReservationDTO;
import com.reservation.reservation.entity.enums.ReservationStatus;
import com.reservation.reservation.service.interfaces.admin.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
@AllArgsConstructor

public class AdminReservationController {
    private final ReservationService reservationService;

    @GetMapping("/all")
    public ResponseEntity<List<ReservationDTO>> getAllReservation(){
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/get/{reservationId}")
    public ResponseEntity<ReservationDTO> getReservationByID(@PathVariable Long reservationId){
        return ResponseEntity.ok(reservationService.getReservationById(reservationId));
    }

    @GetMapping("/get/byStatus/{status}")
    public ResponseEntity<List<ReservationDTO>> getReservationByStatus(@PathVariable ReservationStatus status){
        return ResponseEntity.ok(reservationService.getReservationsByStatus(status));
    }
}

