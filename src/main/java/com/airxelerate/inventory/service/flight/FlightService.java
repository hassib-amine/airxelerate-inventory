package com.airxelerate.inventory.service.flight;

import com.airxelerate.inventory.usecase.request.flight.FlightRequest;
import com.airxelerate.inventory.usecase.response.flight.FlightResponse;

import java.util.List;

/**
 * Flight Service Interface
 * Defines contract for flight management operations
 */
public interface FlightService {
    /**
     * Creates a new flight
     *
     * @param request the flight creation request
     * @return FlightResponse containing the created flight details
     */
    FlightResponse createFlight(FlightRequest request);

    /**
     * Retrieves a flight by its ID
     *
     * @param id the flight ID
     * @return FlightResponse containing the flight details
     */
    FlightResponse getFlightById(Long id);

    /**
     * Retrieves all flights
     *
     * @return List of FlightResponse containing all flight details
     */
    List<FlightResponse> getAllFlights();

    /**
     * Deletes a flight by its ID
     *
     * @param id the flight ID
     */
    void deleteFlight(Long id);
}
