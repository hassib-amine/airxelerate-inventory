package com.airxelerate.inventory.persistence.repository.flight;

import com.airxelerate.inventory.persistence.entity.flight.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightJpaRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByCarrierCode(String carrierCode);
    List<Flight> findByFlightDate(LocalDate flightDate);
    List<Flight> findByOrigin(String origin);
    List<Flight> findByDestination(String destination);
}