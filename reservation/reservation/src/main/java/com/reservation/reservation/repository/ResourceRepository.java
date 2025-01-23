package com.reservation.reservation.repository;

import com.reservation.reservation.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource , Long> {
}
