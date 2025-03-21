package com.reservation.reservation.service.implementation.member;

import com.reservation.reservation.dto.aboutReservations.AboutReservationsDTO;
import com.reservation.reservation.entity.AboutReservations;
import com.reservation.reservation.mapper.abourtReservations.AboutReservationsMapper;
import com.reservation.reservation.repository.AboutReservationsRepository;
import com.reservation.reservation.service.interfaces.member.AboutReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j

public class AboutReservationServiceImpl implements AboutReservationService {

    private final AboutReservationsRepository aboutReservationsRepository;
    private final AboutReservationsMapper aboutReservationsMapper;

    @Override
    @Transactional
    public AboutReservationsDTO create(AboutReservationsDTO aboutReservationsDTO) {
        log.info("Creating new reservation with platform: {} and date: {}",
                aboutReservationsDTO.preferredPlatform(), aboutReservationsDTO.bookingDate());

        // Convert DTO to entity
        AboutReservations aboutReservations = aboutReservationsMapper.toEntity(aboutReservationsDTO);

        // Save entity
        AboutReservations savedReservation = aboutReservationsRepository.save(aboutReservations);
        log.debug("Saved reservation with ID: {}", savedReservation.getId());

        // Convert back to DTO and return
        return aboutReservationsMapper.toDto(savedReservation);
    }
}
