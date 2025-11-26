package com.airxelerate.inventory.service.flight;

import com.airxelerate.inventory.exception.FlightNotFoundException;
import com.airxelerate.inventory.mapper.FlightMapper;
import com.airxelerate.inventory.persistence.entity.flight.Flight;
import com.airxelerate.inventory.persistence.repository.flight.FlightJpaRepository;
import com.airxelerate.inventory.usecase.request.flight.FlightRequest;
import com.airxelerate.inventory.usecase.response.common.PagedResponse;
import com.airxelerate.inventory.usecase.response.flight.FlightResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock
    private FlightJpaRepository flightRepository;

    @Mock
    private FlightMapper flightMapper;

    @InjectMocks
    private FlightServiceImpl flightService;

    private FlightRequest flightRequest;
    private Flight flightEntity;
    private FlightResponse flightResponse;

    @BeforeEach
    void setUp() {
        flightRequest = new FlightRequest(
                "AA",
                "1234",
                LocalDate.of(2025, 1, 1),
                "JFK",
                "LAX"
        );

        flightEntity = Flight.builder()
                .id(1L)
                .carrierCode("AA")
                .flightNumber("1234")
                .flightDate(LocalDate.of(2025, 1, 1))
                .origin("JFK")
                .destination("LAX")
                .build();

        flightResponse = new FlightResponse(
                1L,
                "AA",
                "1234",
                LocalDate.of(2025, 1, 1),
                "JFK",
                "LAX"
        );
    }

    @Test
    void createFlight_shouldMapRequestPersistAndReturnResponse() {
        // given
        given(flightMapper.toEntity(flightRequest)).willReturn(flightEntity);
        given(flightRepository.save(flightEntity)).willReturn(flightEntity);
        given(flightMapper.toResponse(flightEntity)).willReturn(flightResponse);

        // when
        FlightResponse result = flightService.createFlight(flightRequest);

        // then
        assertThat(result).isEqualTo(flightResponse);
        verify(flightMapper).toEntity(flightRequest);
        verify(flightRepository).save(flightEntity);
        verify(flightMapper).toResponse(flightEntity);
    }

    @Test
    void getFlightById_whenFound_shouldReturnMappedResponse() {
        // given
        Long id = 1L;
        given(flightRepository.findByIdAndDeletedFalse(id)).willReturn(Optional.of(flightEntity));
        given(flightMapper.toResponse(flightEntity)).willReturn(flightResponse);

        // when
        FlightResponse result = flightService.getFlightById(id);

        // then
        assertThat(result).isEqualTo(flightResponse);
        verify(flightRepository).findByIdAndDeletedFalse(id);
        verify(flightMapper).toResponse(flightEntity);
    }

    @Test
    void getFlightById_whenNotFound_shouldThrowFlightNotFoundException() {
        // given
        Long id = 42L;
        given(flightRepository.findByIdAndDeletedFalse(id)).willReturn(Optional.empty());

        // when / then
        assertThrows(FlightNotFoundException.class, () -> flightService.getFlightById(id));
        verify(flightRepository).findByIdAndDeletedFalse(id);
    }

    @Test
    void getAllFlights_shouldReturnPagedResponseOfMappedFlights() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> page = new PageImpl<>(List.of(flightEntity), pageable, 1);
        given(flightRepository.findByDeletedFalse(pageable)).willReturn(page);
        given(flightMapper.toResponse(flightEntity)).willReturn(flightResponse);

        // when
        PagedResponse<FlightResponse> result = flightService.getAllFlights(pageable);

        // then
        assertThat(result.content()).containsExactly(flightResponse);
        assertThat(result.totalElements()).isEqualTo(1);
        verify(flightRepository).findByDeletedFalse(pageable);
        verify(flightMapper).toResponse(flightEntity);
    }

    @Test
    void deleteFlight_whenExisting_shouldSoftDelete() {
        // given
        Long id = 1L;
        given(flightRepository.existsByIdAndDeletedFalse(id)).willReturn(true);

        // when
        flightService.deleteFlight(id);

        // then
        verify(flightRepository).existsByIdAndDeletedFalse(id);
        verify(flightRepository).deleteById(id);
    }

    @Test
    void deleteFlight_whenNotExisting_shouldThrowFlightNotFoundException() {
        // given
        Long id = 999L;
        given(flightRepository.existsByIdAndDeletedFalse(id)).willReturn(false);

        // when / then
        assertThrows(FlightNotFoundException.class, () -> flightService.deleteFlight(id));
        verify(flightRepository).existsByIdAndDeletedFalse(eq(id));
    }
}


