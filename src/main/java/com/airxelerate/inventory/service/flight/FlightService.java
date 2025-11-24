package com.airxelerate.inventory.service.flight;

import com.airxelerate.inventory.usecase.request.flight.FlightRequest;
import com.airxelerate.inventory.usecase.response.common.PagedResponse;
import com.airxelerate.inventory.usecase.response.flight.FlightResponse;
import org.springframework.data.domain.Pageable;

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
     * Retrieves all flights with pagination
     *
     * @param pageable pagination parameters (page, size, sort)
     * @return PagedResponse containing paginated flight details
     */
    PagedResponse<FlightResponse> getAllFlights(Pageable pageable);

    /**
     * Deletes a flight by its ID
     *
     * @param id the flight ID
     */
    void deleteFlight(Long id);
}
