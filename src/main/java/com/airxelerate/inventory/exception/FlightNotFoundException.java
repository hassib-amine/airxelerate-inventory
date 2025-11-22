package com.airxelerate.inventory.exception;

/**
 * Exception thrown when a flight is not found in the system
 */
public class FlightNotFoundException extends RuntimeException {

    public FlightNotFoundException() {
        super();
    }

    public FlightNotFoundException(String message) {
        super(message);
    }

    public FlightNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlightNotFoundException(Throwable cause) {
        super(cause);
    }
}
