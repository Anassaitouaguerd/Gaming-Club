package com.reservation.reservation.entity;

import com.reservation.reservation.entity.enums.ResourceStatus;
import com.reservation.reservation.entity.enums.ResourceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ResourceType type;  // BILLIARD_TABLE, FOOSBALL, PC_STATION, VR_STATION

    @Enumerated(EnumType.STRING)
    private ResourceStatus status;  // AVAILABLE, OCCUPIED, MAINTENANCE, DAMAGED

    @ManyToOne
    private Club club;

    @OneToMany(mappedBy = "resource")
    private List<Reservation> reservations;

}
