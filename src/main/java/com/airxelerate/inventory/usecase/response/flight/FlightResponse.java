package com.airxelerate.inventory.usecase.response.flight;

import java.time.LocalDate;

/**
 * Flight Response DTO
 * Represents flight information in API responses
 */
public record FlightResponse(
        Long id,
        String carrierCode,
        String flightNumber,
        LocalDate flightDate,
        String origin,
        String destination
) {
}
