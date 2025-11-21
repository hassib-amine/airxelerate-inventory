package com.airxelerate.inventory.usecase.response.flight;

import com.airxelerate.inventory.persistence.entity.flight.Flight;

import java.time.LocalDate;

public record FlightResponse(
        Long id,
        String carrierCode,
        String flightNumber,
        LocalDate flightDate,
        String origin,
        String destination
) {
    public static FlightResponse fromEntity(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getCarrierCode(),
                flight.getFlightNumber(),
                flight.getFlightDate(),
                flight.getOrigin(),
                flight.getDestination()
        );
    }
}
