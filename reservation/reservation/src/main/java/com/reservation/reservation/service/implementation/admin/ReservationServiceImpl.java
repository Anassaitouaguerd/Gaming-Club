package com.reservation.reservation.service.implementation.admin;

import com.reservation.reservation.dto.ReservationDTO;
import com.reservation.reservation.entity.Reservation;
import com.reservation.reservation.entity.enums.ReservationStatus;
import com.reservation.reservation.mapper.ReservationMapper;
import com.reservation.reservation.repository.ReservationRepository;
import com.reservation.reservation.service.interfaces.admin.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Override
   public List<ReservationDTO> getAllReservations() {
    return reservationRepository.findAll().stream()
            .map(reservationMapper::toDTO)
            .collect(Collectors.toList());
}

    @Override
    public boolean acceptReservation(Long reservationId) {
        return false;
    }

    @Override
    public boolean cancelReservation(Long reservationId) {
        return false;
    }

    @Override
    public Page<Reservation> getReservationsPaginated(Pageable pageable) {
        return null;
    }

    @Override
    public ReservationDTO getReservationById(Long id) {
        Optional<Reservation> existReservation = reservationRepository.findById(id);
        return reservationMapper.toDTO(existReservation);
    }

    @Override
    public List<ReservationDTO> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status).stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
    }
}
