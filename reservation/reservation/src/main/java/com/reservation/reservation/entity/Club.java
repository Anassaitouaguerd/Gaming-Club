package com.reservation.reservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String city;
    private String description;
    private String contactInfo;

    private Long admin;  // The admin who owns/manages this club

    @OneToMany(mappedBy = "club")
    private List<Resource> resources;

    private boolean isApproved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
