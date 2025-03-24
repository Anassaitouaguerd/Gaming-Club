package com.reservation.reservation.repository;

import com.reservation.reservation.entity.Reservation;
import com.reservation.reservation.entity.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatus(ReservationStatus status);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r " +
            "WHERE r.resource.id = :resourceId " +
            "AND ((r.startTime <= :endTime AND r.endTime >= :startTime))")
    boolean existsOverlappingReservation(
            @Param("resourceId") Long resourceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r " +
            "WHERE r.resource.id = :resourceId " +
            "AND r.id != :excludeReservationId " +
            "AND ((r.startTime <= :endTime AND r.endTime >= :startTime))")
    boolean existsOverlappingReservationExcludingCurrent(
            @Param("resourceId") Long resourceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeReservationId") Long excludeReservationId);

    List<Reservation> findByUserIdOrderByStartTimeDesc(Long memberId);

    @Query("SELECT r FROM Reservation r WHERE r.club.id = :clubId ORDER BY r.startTime DESC")
    List<Reservation> findByClubId(@Param("clubId") Long clubId);

    @Query("SELECT r FROM Reservation r WHERE r.club.id IN :clubIds ORDER BY r.startTime DESC")
    List<Reservation> findByClubIdIn(@Param("clubIds") List<Long> clubIds);
}