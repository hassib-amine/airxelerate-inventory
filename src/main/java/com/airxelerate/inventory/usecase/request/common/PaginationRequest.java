package com.airxelerate.inventory.usecase.request.common;

/**
 * Pagination Request Parameters
 * Standard parameters for pagination across all endpoints
 */
public record PaginationRequest(
        int page,
        int size,
        String sort
) {
    /**
     * Default pagination request
     */
    public static PaginationRequest defaults() {
        return new PaginationRequest(0, 20, "id,asc");
    }
}
