package com.airxelerate.inventory.controller.flight;

import com.airxelerate.inventory.service.flight.FlightService;
import com.airxelerate.inventory.usecase.request.flight.FlightRequest;
import com.airxelerate.inventory.usecase.response.common.DeleteResponse;
import com.airxelerate.inventory.usecase.response.common.PagedResponse;
import com.airxelerate.inventory.usecase.response.flight.FlightResponse;
import com.airxelerate.inventory.util.PaginationUtils;
import com.airxelerate.inventory.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    private static final String ENTITY_NAME = "Flight";

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
    @Operation(
            summary = "Get all flights",
            description = "Retrieves a paginated list of all flights. Requires authentication. " +
                    "Supports pagination with 'page' (default: 0), 'size' (default: 20, max: 100), " +
                    "and 'sort' (default: id,asc) parameters."
    )
    public ResponseEntity<PagedResponse<FlightResponse>> getAllFlights(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Page size (max 100)", example = "20")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Sort by field (default: id,asc)", example = "id,asc")
            @RequestParam(required = false) String sort) {
        
        Pageable pageable = PaginationUtils.createPageable(page, size, sort);
        PagedResponse<FlightResponse> response = flightService.getAllFlights(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a flight", description = "Deletes a flight by its ID. Requires ADMIN role.")
    public ResponseEntity<?> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseUtils.deleteNoContent();
    }
}

