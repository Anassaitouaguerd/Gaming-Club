package com.reservation.reservation.mapper.abourtReservations;

import com.reservation.reservation.dto.aboutReservations.AboutReservationsDTO;
import com.reservation.reservation.entity.AboutReservations;
import org.springframework.stereotype.Component;

/**
 * Mapper class to convert between AboutReservations entity and DTO
 */
@Component
public class AboutReservationsMapper {

    public AboutReservationsDTO toDto(AboutReservations entity) {
        if (entity == null) {
            return null;
        }

        return new AboutReservationsDTO(
                entity.getId(),
                entity.getExperienceLevel(),
                entity.getPreferredPlatform(),
                entity.getBookingDate(),
                entity.getDuration(),
                entity.getStationNumber(),
                entity.getAdditionalRequests(),
                entity.getGamePreferences()
        );
    }

    public AboutReservations toEntity(AboutReservationsDTO dto) {
        if (dto == null) {
            return null;
        }

        AboutReservations entity = AboutReservations.builder()
                .experienceLevel(dto.experienceLevel())
                .preferredPlatform(dto.preferredPlatform())
                .bookingDate(dto.bookingDate())
                .duration(dto.duration())
                .stationNumber(dto.stationNumber())
                .additionalRequests(dto.additionalRequests())
                .gamePreferences(dto.gamePreferences())
                .build();

        if (dto.id() != null) {
            entity.setId(dto.id());
        }

        return entity;
    }
}