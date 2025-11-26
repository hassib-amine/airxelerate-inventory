package com.airxelerate.inventory.persistence.repository.flight;

import com.airxelerate.inventory.persistence.entity.flight.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightJpaRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByCarrierCode(String carrierCode);
    List<Flight> findByFlightDate(LocalDate flightDate);
    List<Flight> findByOrigin(String origin);
    List<Flight> findByDestination(String destination);

    // Soft-delete-aware methods
    Optional<Flight> findByIdAndDeletedFalse(Long id);

    boolean existsByIdAndDeletedFalse(Long id);

    Page<Flight> findByDeletedFalse(Pageable pageable);
}