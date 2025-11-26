package com.airxelerate.inventory.service.flight;

import com.airxelerate.inventory.exception.FlightNotFoundException;
import com.airxelerate.inventory.mapper.FlightMapper;
import com.airxelerate.inventory.persistence.entity.flight.Flight;
import com.airxelerate.inventory.persistence.repository.flight.FlightJpaRepository;
import com.airxelerate.inventory.usecase.request.flight.FlightRequest;
import com.airxelerate.inventory.usecase.response.common.PagedResponse;
import com.airxelerate.inventory.usecase.response.flight.FlightResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;

/**
 * Flight Service Implementation
 * Handles business logic for flight operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightJpaRepository flightRepository;
    private final FlightMapper flightMapper;

    @Override
    @Transactional
    @CacheEvict(value = "flights", allEntries = true)
    public FlightResponse createFlight(FlightRequest request) {
        Flight flight = flightMapper.toEntity(request);
        Flight savedFlight = flightRepository.save(flight);
        return flightMapper.toResponse(savedFlight);
    }

    @Override
    @Transactional(readOnly = true)
    public FlightResponse getFlightById(Long id) {
        log.debug("Retrieving flight with id={}", id);
        Flight flight = flightRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.warn("Flight not found (or soft deleted) with id={}", id);
                    return new FlightNotFoundException("Flight not found with id: " + id);
                });
        log.debug("Flight found with id={}", id);
        return flightMapper.toResponse(flight);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "flights", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public PagedResponse<FlightResponse> getAllFlights(Pageable pageable) {
        log.debug("Retrieving flights page: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Page<Flight> flightPage = flightRepository.findByDeletedFalse(pageable);
        Page<FlightResponse> responsePage = flightPage.map(flightMapper::toResponse);
        log.debug("Retrieved {} flights (totalElements={})",
                responsePage.getNumberOfElements(), responsePage.getTotalElements());
        return PagedResponse.of(responsePage);
    }

    @Override
    @Transactional
    @CacheEvict(value = "flights", allEntries = true)
    public void deleteFlight(Long id) {
        log.info("Soft deleting flight with id={}", id);
        if (!flightRepository.existsByIdAndDeletedFalse(id)) {
            log.warn("Attempted to soft delete non-existing or already deleted flight with id={}", id);
            throw new FlightNotFoundException("Flight not found with id: " + id);
        }
        // Triggers @SQLDelete → sets deleted = true (soft delete)
        flightRepository.deleteById(id);
        log.info("Flight with id={} soft deleted successfully", id);
    }
}

