package com.airxelerate.inventory.persistence.entity.flight;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "flights", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"carrier_code", "flight_number", "flight_date"})
})
@SQLDelete(sql = "UPDATE flights SET deleted = true WHERE id = ?")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "carrier_code", nullable = false, length = 2)
    @Pattern(regexp = "^[A-Z]{2}$", message = "Carrier code must be 2 uppercase letters")
    @NotBlank(message = "Carrier code is required")
    private String carrierCode;

    @Column(name = "flight_number", nullable = false, length = 4)
    @Pattern(regexp = "^\\d{4}$", message = "Flight number must be 4 digits")
    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @Column(name = "flight_date", nullable = false)
    private LocalDate flightDate;

    @Column(name = "origin", nullable = false, length = 3)
    @Pattern(regexp = "^[A-Z]{3}$", message = "Origin must be 3 uppercase letters")
    @NotBlank(message = "Origin is required")
    private String origin;

    @Column(name = "destination", nullable = false, length = 3)
    @Pattern(regexp = "^[A-Z]{3}$", message = "Destination must be 3 uppercase letters")
    @NotBlank(message = "Destination is required")
    private String destination;

    /**
     * Soft delete flag.
     * When true, the record is considered deleted.
     */
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
}