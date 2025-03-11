package com.reservation.reservation.service.implementation.member;

import com.reservation.reservation.dto.request.ReservationRequest;
import com.reservation.reservation.dto.request.ReservationResponse;
import com.reservation.reservation.dto.request.ResourceResponse;
import com.reservation.reservation.entity.Resource;
import com.reservation.reservation.entity.Reservation;
import com.reservation.reservation.entity.enums.ResourceStatus;
import com.reservation.reservation.entity.enums.ReservationStatus;
import com.reservation.reservation.exceptions.ResourceNotFoundException;
import com.reservation.reservation.exceptions.InvalidReservationException;
import com.reservation.reservation.mapper.request.MemberReservationMapper;
import com.reservation.reservation.mapper.request.MemberResourceMapper;
import com.reservation.reservation.repository.ReservationRepository;
import com.reservation.reservation.repository.ResourceRepository;
import com.reservation.reservation.service.interfaces.member.ReservationMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationMemberServiceImpl implements ReservationMemberService {

    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;
    private final MemberReservationMapper reservationMapper;
    private final MemberResourceMapper resourceMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> getAvailableResources() {
        Optional<List<Resource>> availableResources = resourceRepository.findByStatus(ResourceStatus.AVAILABLE);
        return resourceMapper.toResponseList(availableResources);
    }

    @Override
    @Transactional
    public ReservationResponse makeReservation(ReservationRequest request, Long userId) {
        validateReservationTimes(request.startTime(), request.endTime());

        Resource resource = resourceRepository.findById(request.resourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + request.resourceId()));

        if (resource.getStatus() != ResourceStatus.AVAILABLE) {
            throw new InvalidReservationException("Resource is not available for reservation");
        }

        // Check for conflicting reservations
        boolean hasConflict = reservationRepository.existsOverlappingReservation(
                request.resourceId(),
                request.startTime(),
                request.endTime()
        );

        if (hasConflict) {
            throw new InvalidReservationException("Requested time slot is already booked");
        }

        Reservation reservation = Reservation.builder()
                .userId(userId)
                .resource(resource)
                .startTime(request.startTime())
                .endTime(request.endTime())
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toResponse(savedReservation);
    }

    @Override
    @Transactional
    public ReservationResponse cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new InvalidReservationException("Reservation is not active and cannot be cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setUpdatedAt(LocalDateTime.now());
        Reservation updatedReservation = reservationRepository.save(reservation);

        return reservationMapper.toResponse(updatedReservation);
    }

    @Override
    @Transactional
    public ReservationResponse modifyReservation(Long reservationId, ReservationRequest request) {
        validateReservationTimes(request.startTime(), request.endTime());

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new InvalidReservationException("Only active reservations can be modified");
        }

        // Check for conflicting reservations, excluding the current reservation
        boolean hasConflict = reservationRepository.existsOverlappingReservationExcludingCurrent(
                reservation.getResource().getId(),
                request.startTime(),
                request.endTime(),
                reservationId
        );

        if (hasConflict) {
            throw new InvalidReservationException("Requested time slot is already booked");
        }

        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.endTime());
        reservation.setUpdatedAt(LocalDateTime.now());

        Reservation updatedReservation = reservationRepository.save(reservation);
        return reservationMapper.toResponse(updatedReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getUserReservations(Long userId) {
        List<Reservation> userReservations = reservationRepository.findByUserIdOrderByStartTimeDesc(userId);
        return reservationMapper.toResponseList(userReservations);
    }

    private void validateReservationTimes(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();

        if (startTime == null || endTime == null) {
            throw new InvalidReservationException("Start time and end time must not be null");
        }

        if (startTime.isBefore(now)) {
            throw new InvalidReservationException("Start time must be in the future");
        }

        if (endTime.isBefore(startTime)) {
            throw new InvalidReservationException("End time must be after start time");
        }

        if (startTime.plusHours(24).isBefore(endTime)) {
            throw new InvalidReservationException("Reservation duration cannot exceed 24 hours");
        }
    }
}