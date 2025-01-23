package com.reservation.reservation.repository;

import com.reservation.reservation.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club , Long> {
}
