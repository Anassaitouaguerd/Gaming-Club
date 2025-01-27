package com.reservation.reservation.entity;

import com.reservation.reservation.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    private Resource resource;

    @ManyToOne
    private Club club;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;  // PENDING, CONFIRMED, CANCELLED, COMPLETED

    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long staffHandler;
}
