package com.airxelerate.inventory.service.flight;

import com.airxelerate.inventory.exception.FlightNotFoundException;
import com.airxelerate.inventory.mapper.FlightMapper;
import com.airxelerate.inventory.persistence.entity.flight.Flight;
import com.airxelerate.inventory.persistence.repository.flight.FlightJpaRepository;
import com.airxelerate.inventory.usecase.request.flight.FlightRequest;
import com.airxelerate.inventory.usecase.response.flight.FlightResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Flight Service Implementation
 * Handles business logic for flight operations
 */
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightJpaRepository flightRepository;
    private final FlightMapper flightMapper;

    @Override
    @Transactional
    public FlightResponse createFlight(FlightRequest request) {
        Flight flight = flightMapper.toEntity(request);
        Flight savedFlight = flightRepository.save(flight);
        return flightMapper.toResponse(savedFlight);
    }

    @Override
    @Transactional(readOnly = true)
    public FlightResponse getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));
        return flightMapper.toResponse(flight);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightResponse> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        return flightMapper.toResponseList(flights);
    }

    @Override
    @Transactional
    public void deleteFlight(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new FlightNotFoundException("Flight not found with id: " + id);
        }
        flightRepository.deleteById(id);
    }
}

