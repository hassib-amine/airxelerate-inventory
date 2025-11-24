package com.airxelerate.inventory.usecase.response.common;

import java.time.LocalDateTime;

/**
 * Standard Delete Response DTO
 * Provides consistent response structure for delete operations
 */
public record DeleteResponse(
        String message,
        Long deletedId,
        LocalDateTime deletedAt,
        boolean success
) {
    /**
     * Creates a successful delete response
     *
     * @param deletedId the ID of the deleted entity
     * @param entityName the name of the entity type (e.g., "Flight")
     * @return DeleteResponse with success status
     */
    public static DeleteResponse success(Long deletedId, String entityName) {
        return new DeleteResponse(
                String.format("%s with id %d deleted successfully", entityName, deletedId),
                deletedId,
                LocalDateTime.now(),
                true
        );
    }

    /**
     * Creates a successful delete response with custom message
     *
     * @param deletedId the ID of the deleted entity
     * @param message custom success message
     * @return DeleteResponse with success status
     */
    public static DeleteResponse success(Long deletedId, String message, boolean useCustomMessage) {
        return new DeleteResponse(
                message,
                deletedId,
                LocalDateTime.now(),
                true
        );
    }
}
