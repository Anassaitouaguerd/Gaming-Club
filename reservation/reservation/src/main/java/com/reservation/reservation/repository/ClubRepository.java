package com.reservation.reservation.repository;

import com.reservation.reservation.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club , Long> {
    List<Club> findByCity(String cityName);
}
