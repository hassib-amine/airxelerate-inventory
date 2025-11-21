package com.airxelerate.inventory.usecase.request.flight;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record FlightRequest(
        @NotBlank(message = "Carrier code is required")
        @Pattern(regexp = "^[A-Z]{2}$", message = "Carrier code must be 2 uppercase letters (e.g., AA, BA)")
        String carrierCode,

        @NotBlank(message = "Flight number is required")
        @Pattern(regexp = "^\\d{4}$", message = "Flight number must be 4 digits (e.g., 1234)")
        String flightNumber,

        @NotNull(message = "Flight date is required")
        LocalDate flightDate,

        @NotBlank(message = "Origin is required")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Origin must be 3 uppercase letters (e.g., JFK, LAX)")
        String origin,

        @NotBlank(message = "Destination is required")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Destination must be 3 uppercase letters (e.g., JFK, LAX)")
        String destination
) {}