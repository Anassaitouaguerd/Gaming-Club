package com.reservation.reservation.entity;

import com.reservation.reservation.entity.enums.ResourceStatus;
import com.reservation.reservation.entity.enums.ResourceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Enumerated(EnumType.STRING)
    private ResourceStatus status;

    @ManyToOne
    private Club club;

    @OneToMany(mappedBy = "resource")
    private List<Reservation> reservations;

}
