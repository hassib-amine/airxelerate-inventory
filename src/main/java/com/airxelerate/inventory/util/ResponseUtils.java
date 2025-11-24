package com.airxelerate.inventory.util;

import com.airxelerate.inventory.usecase.response.common.DeleteResponse;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for creating standardized HTTP responses
 * Provides reusable methods for common response patterns
 */
public final class ResponseUtils {

    private ResponseUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Creates a successful delete response with 200 OK status
     *
     * @param deletedId the ID of the deleted entity
     * @param entityName the name of the entity type (e.g., "Flight")
     * @return ResponseEntity with DeleteResponse
     */
    public static ResponseEntity<DeleteResponse> deleteSuccess(Long deletedId, String entityName) {
        DeleteResponse response = DeleteResponse.success(deletedId, entityName);
        return ResponseEntity.ok(response);
    }



    /**
     * Creates a successful delete response with 204 No Content status
     * Use this if you prefer the traditional REST approach (no response body)
     *
     * @return ResponseEntity with no content
     */
    public static ResponseEntity<Void> deleteNoContent() {
        return ResponseEntity.noContent().build();
    }

    /**
     * Creates a successful delete response with 200 OK status
     * Alternative to deleteNoContent() if you want to return a response body
     *
     * @param deletedId the ID of the deleted entity
     * @param entityName the name of the entity type
     * @return ResponseEntity with DeleteResponse
     */
    public static ResponseEntity<DeleteResponse> deleteOk(Long deletedId, String entityName) {
        return deleteSuccess(deletedId, entityName);
    }
}
