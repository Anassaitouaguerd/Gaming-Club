package com.reservation.reservation.repository;

import com.reservation.reservation.entity.Club;
import com.reservation.reservation.entity.Resource;
import com.reservation.reservation.entity.enums.ResourceStatus;
import com.reservation.reservation.entity.enums.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource , Long> {
    boolean existsByNameAndClub(String name, Club existingClub);

    Optional<List<Resource>> findByStatus(ResourceStatus resourceStatus);

    Optional<Object> findByClub(Club club);

    Optional<Object> findByType(ResourceType resourceType);
}

