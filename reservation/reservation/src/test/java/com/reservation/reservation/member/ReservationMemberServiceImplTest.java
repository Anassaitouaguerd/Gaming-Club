package com.reservation.reservation.member;

import com.reservation.reservation.dto.request.ReservationRequest;
import com.reservation.reservation.dto.request.ReservationResponse;
import com.reservation.reservation.dto.request.ResourceResponse;
import com.reservation.reservation.entity.Club;
import com.reservation.reservation.entity.Reservation;
import com.reservation.reservation.entity.Resource;
import com.reservation.reservation.entity.enums.ReservationStatus;
import com.reservation.reservation.entity.enums.ResourceStatus;
import com.reservation.reservation.exceptions.InvalidReservationException;
import com.reservation.reservation.exceptions.ResourceNotFoundException;
import com.reservation.reservation.mapper.request.MemberReservationMapper;
import com.reservation.reservation.mapper.request.MemberResourceMapper;
import com.reservation.reservation.repository.ClubRepository;
import com.reservation.reservation.repository.ReservationRepository;
import com.reservation.reservation.repository.ResourceRepository;
import com.reservation.reservation.service.implementation.member.ReservationMemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationMemberServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private MemberReservationMapper reservationMapper;

    @Mock
    private MemberResourceMapper resourceMapper;

    @InjectMocks
    private ReservationMemberServiceImpl reservationService;

    private Resource resource;
    private Club club;
    private Reservation reservation;
    private ReservationRequest reservationRequest;
    private ReservationResponse reservationResponse;
    private ResourceResponse resourceResponse;
    private LocalDateTime now;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long userId;
    private Long resourceId;
    private Long clubId;
    private Long reservationId;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        startTime = now.plusHours(1);
        endTime = now.plusHours(2);
        userId = 1L;
        resourceId = 1L;
        clubId = 1L;
        reservationId = 1L;

        resource = new Resource();
        resource.setId(resourceId);
        resource.setName("Test Resource");
        resource.setStatus(ResourceStatus.AVAILABLE);

        club = new Club();
        club.setId(clubId);
        club.setName("Test Club");

        reservation = Reservation.builder()
                .id(reservationId)
                .userId(userId)
                .resource(resource)
                .club(club)
                .startTime(startTime)
                .endTime(endTime)
                .status(ReservationStatus.ACTIVE)
                .createdAt(now)
                .build();

        reservationRequest = new ReservationRequest(
                clubId,
                resourceId,
                startTime,
                endTime
        );

        reservationResponse = ReservationResponse.builder()
                .id(reservationId)
                .resourceId(resourceId)
                .resourceName("Test Resource")
                .startTime(startTime)
                .endTime(endTime)
                .status(ReservationStatus.ACTIVE)
                .createdAt(now)
                .build();

        resourceResponse = ResourceResponse.builder()
                .id(resourceId)
                .name("Test Resource")
                .status(ResourceStatus.AVAILABLE)
                .build();
    }

    @Test
    void getAvailableResources_ShouldReturnResourceResponseList() {
        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(resource);
        Optional<List<Resource>> optionalResources = Optional.of(resourceList);

        List<ResourceResponse> resourceResponses = new ArrayList<>();
        resourceResponses.add(resourceResponse);

        when(resourceRepository.findByStatus(ResourceStatus.AVAILABLE)).thenReturn(optionalResources);
        when(resourceMapper.toResponseList(optionalResources)).thenReturn(resourceResponses);

        List<ResourceResponse> result = reservationService.getAvailableResources();

        assertEquals(resourceResponses, result);
        verify(resourceRepository).findByStatus(ResourceStatus.AVAILABLE);
        verify(resourceMapper).toResponseList(optionalResources);
    }

    @Test
    void makeReservation_ShouldCreateReservation_WhenValidRequest() {
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));
        when(reservationRepository.existsOverlappingReservation(resourceId, startTime, endTime)).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponse(reservation)).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.makeReservation(reservationRequest, userId);

        assertEquals(reservationResponse, result);
        verify(resourceRepository).findById(resourceId);
        verify(clubRepository).findById(clubId);
        verify(reservationRepository).existsOverlappingReservation(resourceId, startTime, endTime);
        verify(reservationRepository).save(any(Reservation.class));
        verify(reservationMapper).toResponse(any(Reservation.class));
    }

    @Test
    void makeReservation_ShouldThrowException_WhenResourceNotFound() {
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                reservationService.makeReservation(reservationRequest, userId)
        );

        verify(resourceRepository).findById(resourceId);
        verify(clubRepository, never()).findById(anyLong());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void makeReservation_ShouldThrowException_WhenClubNotFound() {
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(clubRepository.findById(clubId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                reservationService.makeReservation(reservationRequest, userId)
        );

        verify(resourceRepository).findById(resourceId);
        verify(clubRepository).findById(clubId);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void makeReservation_ShouldThrowException_WhenResourceNotAvailable() {
        resource.setStatus(ResourceStatus.OCCUPIED);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));

        assertThrows(InvalidReservationException.class, () ->
                reservationService.makeReservation(reservationRequest, userId)
        );

        verify(resourceRepository).findById(resourceId);
        verify(clubRepository).findById(clubId);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void makeReservation_ShouldThrowException_WhenTimeSlotConflict() {
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));
        when(reservationRepository.existsOverlappingReservation(resourceId, startTime, endTime)).thenReturn(true);

        assertThrows(InvalidReservationException.class, () ->
                reservationService.makeReservation(reservationRequest, userId)
        );

        verify(resourceRepository).findById(resourceId);
        verify(clubRepository).findById(clubId);
        verify(reservationRepository).existsOverlappingReservation(resourceId, startTime, endTime);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void cancelReservation_ShouldCancelReservation_WhenValid() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponse(reservation)).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.cancelReservation(reservationId);

        assertEquals(reservationResponse, result);
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        assertNotNull(reservation.getUpdatedAt());
        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository).save(reservation);
        verify(reservationMapper).toResponse(reservation);
    }

    @Test
    void cancelReservation_ShouldThrowException_WhenReservationNotFound() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                reservationService.cancelReservation(reservationId)
        );

        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void cancelReservation_ShouldThrowException_WhenReservationNotActive() {
        reservation.setStatus(ReservationStatus.CANCELLED);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        assertThrows(InvalidReservationException.class, () ->
                reservationService.cancelReservation(reservationId)
        );

        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void modifyReservation_ShouldUpdateReservation_WhenValid() {
        LocalDateTime newStartTime = now.plusHours(3);
        LocalDateTime newEndTime = now.plusHours(4);
        ReservationRequest modifyRequest = new ReservationRequest(clubId, resourceId, newStartTime, newEndTime);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.existsOverlappingReservationExcludingCurrent(
                resourceId, newStartTime, newEndTime, reservationId)).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponse(reservation)).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.modifyReservation(reservationId, modifyRequest);

        assertEquals(reservationResponse, result);
        assertEquals(newStartTime, reservation.getStartTime());
        assertEquals(newEndTime, reservation.getEndTime());
        assertNotNull(reservation.getUpdatedAt());
        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository).existsOverlappingReservationExcludingCurrent(
                resourceId, newStartTime, newEndTime, reservationId);
        verify(reservationRepository).save(reservation);
        verify(reservationMapper).toResponse(reservation);
    }

    @Test
    void modifyReservation_ShouldThrowException_WhenReservationNotFound() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                reservationService.modifyReservation(reservationId, reservationRequest)
        );

        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void modifyReservation_ShouldThrowException_WhenReservationNotActive() {
        reservation.setStatus(ReservationStatus.CANCELLED);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        assertThrows(InvalidReservationException.class, () ->
                reservationService.modifyReservation(reservationId, reservationRequest)
        );

        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void modifyReservation_ShouldThrowException_WhenTimeSlotConflict() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.existsOverlappingReservationExcludingCurrent(
                resourceId, startTime, endTime, reservationId)).thenReturn(true);

        assertThrows(InvalidReservationException.class, () ->
                reservationService.modifyReservation(reservationId, reservationRequest)
        );

        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository).existsOverlappingReservationExcludingCurrent(
                resourceId, startTime, endTime, reservationId);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void getUserReservations_ShouldReturnUserReservations() {
        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);

        List<ReservationResponse> responseList = new ArrayList<>();
        responseList.add(reservationResponse);

        when(reservationRepository.findByUserIdOrderByStartTimeDesc(userId)).thenReturn(reservationList);
        when(reservationMapper.toResponseList(reservationList)).thenReturn(responseList);

        List<ReservationResponse> result = reservationService.getUserReservations(userId);

        assertEquals(responseList, result);
        verify(reservationRepository).findByUserIdOrderByStartTimeDesc(userId);
        verify(reservationMapper).toResponseList(reservationList);
    }

    @Test
    void validateReservationTimes_ShouldThrowException_WhenStartTimeInPast() {
        LocalDateTime pastStartTime = now.minusHours(1);
        ReservationRequest invalidRequest = new ReservationRequest(
                clubId, resourceId, pastStartTime, endTime
        );

        assertThrows(InvalidReservationException.class, () ->
                reservationService.makeReservation(invalidRequest, userId)
        );
    }

    @Test
    void validateReservationTimes_ShouldThrowException_WhenEndTimeBeforeStartTime() {
        LocalDateTime invalidEndTime = startTime.minusHours(1);
        ReservationRequest invalidRequest = new ReservationRequest(
                clubId, resourceId, startTime, invalidEndTime
        );

        assertThrows(InvalidReservationException.class, () ->
                reservationService.makeReservation(invalidRequest, userId)
        );
    }

    @Test
    void validateReservationTimes_ShouldThrowException_WhenDurationExceeds24Hours() {
        LocalDateTime invalidEndTime = startTime.plusHours(25);
        ReservationRequest invalidRequest = new ReservationRequest(
                clubId, resourceId, startTime, invalidEndTime
        );

        assertThrows(InvalidReservationException.class, () ->
                reservationService.makeReservation(invalidRequest, userId)
        );
    }
}