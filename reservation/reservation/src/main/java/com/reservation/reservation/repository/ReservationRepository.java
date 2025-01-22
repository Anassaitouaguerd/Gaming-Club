package com.reservation.reservation.repository;

import com.reservation.reservation.entity.Reservation;
import com.reservation.reservation.entity.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation , Long> {
    List<Reservation> findByStatus(ReservationStatus status);
}
