package com.airxelerate.inventory.controller.flight;

import com.airxelerate.inventory.service.flight.FlightService;
import com.airxelerate.inventory.usecase.request.flight.FlightRequest;
import com.airxelerate.inventory.usecase.response.flight.FlightResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Flight Controller
 * Handles flight CRUD operations
 */
@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@Tag(name = "Flights", description = "Flight management API endpoints")
@SecurityRequirement(name = "bearerAuth")
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a new flight", description = "Creates a new flight. Requires ADMIN role.")
    public ResponseEntity<FlightResponse> createFlight(@Valid @RequestBody FlightRequest request) {
        FlightResponse response = flightService.createFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get flight by ID", description = "Retrieves a flight by its ID. Requires authentication.")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable Long id) {
        FlightResponse response = flightService.getFlightById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all flights", description = "Retrieves a list of all flights. Requires authentication.")
    public ResponseEntity<List<FlightResponse>> getAllFlights() {
        List<FlightResponse> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a flight", description = "Deletes a flight by its ID. Requires ADMIN role.")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}

