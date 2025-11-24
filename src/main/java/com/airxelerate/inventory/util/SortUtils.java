package com.airxelerate.inventory.util;

import org.springframework.data.domain.Sort;

/**
 * Utility class for sorting operations
 * Provides reusable methods for parsing and creating Sort objects
 */
public final class SortUtils {

    private SortUtils() {
        // Utility class - prevent instantiation
    }

    // Default sort constants
    public static final String DEFAULT_SORT_FIELD = "id";
    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    /**
     * Parses sort parameter into Sort object
     * Supports formats:
     * - "field,direction" (e.g., "flightDate,desc")
     * - "field" (defaults to ascending)
     * - null or empty (defaults to "id,asc")
     *
     * @param sort sort parameter string
     * @return Sort object
     */
    public static Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORT_FIELD);
        }

        String trimmedSort = sort.trim();
        String[] parts = trimmedSort.split(",");

        String field = parts[0].trim();
        if (field.isEmpty()) {
            return Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORT_FIELD);
        }

        // If only field is provided, default to ascending
        if (parts.length == 1) {
            return Sort.by(DEFAULT_SORT_DIRECTION, field);
        }

        // Parse direction
        String directionStr = parts[1].trim().toUpperCase();
        Sort.Direction direction = parseDirection(directionStr);

        return Sort.by(direction, field);
    }

    /**
     * Parses multiple sort fields
     * Format: "field1,direction1;field2,direction2" or "field1;field2"
     *
     * @param sort multi-field sort parameter
     * @return Sort object with multiple sort orders
     */
    public static Sort parseMultiSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORT_FIELD);
        }

        String[] sortFields = sort.split(";");
        Sort result = null;

        for (String sortField : sortFields) {
            Sort currentSort = parseSort(sortField.trim());
            if (result == null) {
                result = currentSort;
            } else {
                result = result.and(currentSort);
            }
        }

        return result != null ? result : Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORT_FIELD);
    }

    /**
     * Parses direction string to Sort.Direction enum
     *
     * @param direction direction string (ASC, DESC, or any case variation)
     * @return Sort.Direction (defaults to ASC if invalid)
     */
    private static Sort.Direction parseDirection(String direction) {
        if (direction == null || direction.isBlank()) {
            return DEFAULT_SORT_DIRECTION;
        }

        return "DESC".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
    }

    /**
     * Validates if a field is allowed for sorting
     * Override this method in subclasses or use a whitelist approach
     *
     * @param field field name to validate
     * @param allowedFields array of allowed field names
     * @return true if field is allowed
     */
    public static boolean isFieldAllowed(String field, String... allowedFields) {
        if (field == null || field.isBlank()) {
            return false;
        }

        for (String allowedField : allowedFields) {
            if (field.equalsIgnoreCase(allowedField)) {
                return true;
            }
        }
        return false;
    }
}
