package com.reservation.reservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "about_reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AboutReservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "experience_level", nullable = false)
    private String experienceLevel;

    @Column(name = "preferred_platform", nullable = false)
    private String preferredPlatform;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "station_number", nullable = false)
    private Integer stationNumber;

    @Column(name = "additional_requests", columnDefinition = "TEXT")
    private String additionalRequests;

    @ElementCollection
    @CollectionTable(
            name = "reservation_game_preferences",
            joinColumns = @JoinColumn(name = "reservation_id")
    )
    @Column(name = "game_preference")
    private List<String> gamePreferences;


}
