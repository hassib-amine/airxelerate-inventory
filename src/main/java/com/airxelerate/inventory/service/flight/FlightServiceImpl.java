package com.airxelerate.inventory.service.flight;

import com.airxelerate.inventory.exception.FlightNotFoundException;
import com.airxelerate.inventory.mapper.FlightMapper;
import com.airxelerate.inventory.persistence.entity.flight.Flight;
import com.airxelerate.inventory.persistence.repository.flight.FlightJpaRepository;
import com.airxelerate.inventory.usecase.request.flight.FlightRequest;
import com.airxelerate.inventory.usecase.response.common.PagedResponse;
import com.airxelerate.inventory.usecase.response.flight.FlightResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Flight Service Implementation
 * Handles business logic for flight operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
    public PagedResponse<FlightResponse> getAllFlights(Pageable pageable) {
        Page<Flight> flightPage = flightRepository.findAll(pageable);
        Page<FlightResponse> responsePage = flightPage.map(flightMapper::toResponse);
        return PagedResponse.of(responsePage);
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

