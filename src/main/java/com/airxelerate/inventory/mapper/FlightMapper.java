package com.airxelerate.inventory.mapper;

import com.airxelerate.inventory.persistence.entity.flight.Flight;
import com.airxelerate.inventory.usecase.request.flight.FlightRequest;
import com.airxelerate.inventory.usecase.response.flight.FlightResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct Mapper for Flight entity conversions
 * Handles mapping between Flight entity, FlightRequest, and FlightResponse
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FlightMapper {

    /**
     * Maps FlightRequest to Flight entity
     * Uses Flight's builder pattern (from Lombok @Builder)
     *
     * @param request the flight request DTO
     * @return Flight entity
     */
    default Flight toEntity(FlightRequest request) {
        if (request == null) {
            return null;
        }
        return Flight.builder()
                .carrierCode(request.carrierCode())
                .flightNumber(request.flightNumber())
                .flightDate(request.flightDate())
                .origin(request.origin())
                .destination(request.destination())
                .build();
    }

    /**
     * Maps Flight entity to FlightResponse
     *
     * @param flight the flight entity
     * @return FlightResponse DTO
     */
    FlightResponse toResponse(Flight flight);

    /**
     * Maps list of Flight entities to list of FlightResponse
     *
     * @param flights list of flight entities
     * @return list of FlightResponse DTOs
     */
    List<FlightResponse> toResponseList(List<Flight> flights);
}

