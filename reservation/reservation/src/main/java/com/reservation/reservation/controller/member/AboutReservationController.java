package com.reservation.reservation.controller.member;

import com.reservation.reservation.dto.aboutReservations.AboutReservationsDTO;
import com.reservation.reservation.service.interfaces.member.AboutReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member/about/reservation")
@RequiredArgsConstructor

public class AboutReservationController {

    private final AboutReservationService aboutReservationService;

    @PostMapping("/create")
    public ResponseEntity<AboutReservationsDTO> createReservation(@RequestBody AboutReservationsDTO aboutReservationsDTO) {
        AboutReservationsDTO createdReservation = aboutReservationService.create(aboutReservationsDTO);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }
}
