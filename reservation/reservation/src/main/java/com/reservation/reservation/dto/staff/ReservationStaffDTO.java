package com.reservation.reservation.dto.staff;

import lombok.Builder;

@Builder
public record ReservationStaffDTO(
        Long id,
        Long clubId,
        String clubName,
        Long customerId,
        String customerName,
        String customerEmail,
        String customerPhone,
        String date,
        String startTime,
        String endTime,
        Integer duration,
        String station,
        String status,
        Double totalPrice,
        String paymentMethod,
        String createdAt
) {
}
