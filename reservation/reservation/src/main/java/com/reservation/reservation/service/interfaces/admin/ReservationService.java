package com.reservation.reservation.service.interfaces.admin;

import com.reservation.reservation.dto.ReservationDTO;
import com.reservation.reservation.entity.Reservation;
import com.reservation.reservation.entity.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {

    List<ReservationDTO> getAllReservations();
//    List<Reservation> getReservationsByFilter(Map<String, Object> filters);

    boolean acceptReservation(Long reservationId);
    boolean cancelReservation(Long reservationId);

//    boolean checkConflicts(Reservation reservation);
//    boolean resolveConflict(Long reservationId, ConflictResolution resolution);


    Page<Reservation> getReservationsPaginated(Pageable pageable);
//    ReservationStats getReservationStatistics(LocalDateTime startDate, LocalDateTime endDate);

    ReservationDTO getReservationById(Long id);
    List<ReservationDTO> getReservationsByStatus(ReservationStatus status);
}
