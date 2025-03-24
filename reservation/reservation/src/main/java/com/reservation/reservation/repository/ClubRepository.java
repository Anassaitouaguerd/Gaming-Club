package com.reservation.reservation.repository;

import com.reservation.reservation.dto.ClubDTO;
import com.reservation.reservation.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club , Long> {
    List<Club> findByCity(String cityName);

    @Query("SELECT c FROM Club c WHERE c.admin = :staffId")
    List<Club> findClubsByStaffId(@Param("staffId") Long staffId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Club c  WHERE c.id = :clubId AND c.admin = :staffId")
    boolean checkClubBelongsToStaff(@Param("clubId") Long clubId, @Param("staffId") Long staffId);
}
