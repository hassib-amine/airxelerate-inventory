package com.airxelerate.inventory.util;

import com.airxelerate.inventory.usecase.request.common.PaginationRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Utility class for pagination operations
 * Provides reusable methods for creating Pageable objects with validation and defaults
 */
public final class PaginationUtils {

    private PaginationUtils() {
        // Utility class - prevent instantiation
    }

    // Default pagination constants
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;
    public static final int MIN_SIZE = 1;
    public static final String DEFAULT_SORT = "id,asc";

    /**
     * Creates a Pageable object with default values
     *
     * @return Pageable with default pagination settings
     */
    public static Pageable createDefaultPageable() {
        return createPageable(DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT);
    }

    /**
     * Creates a Pageable object with validation and defaults
     *
     * @param page page number (0-indexed)
     * @param size page size
     * @param sort sort parameter (format: "field,direction" or "field")
     * @return validated Pageable object
     */
    public static Pageable createPageable(Integer page, Integer size, String sort) {
        int validatedPage = validatePage(page);
        int validatedSize = validateSize(size);
        Sort sortObj = SortUtils.parseSort(sort);

        return PageRequest.of(validatedPage, validatedSize, sortObj);
    }

    /**
     * Creates a Pageable object from PaginationRequest
     *
     * @param request pagination request parameters
     * @return validated Pageable object
     */
    public static Pageable createPageable(PaginationRequest request) {
        if (request == null) {
            return createDefaultPageable();
        }
        return createPageable(request.page(), request.size(), request.sort());
    }

    /**
     * Validates and normalizes page number
     *
     * @param page page number (can be null)
     * @return validated page number (defaults to 0 if null or negative)
     */
    public static int validatePage(Integer page) {
        if (page == null || page < 0) {
            return DEFAULT_PAGE;
        }
        return page;
    }

    /**
     * Validates and normalizes page size
     * Ensures size is between MIN_SIZE and MAX_SIZE
     *
     * @param size page size (can be null)
     * @return validated page size
     */
    public static int validateSize(Integer size) {
        if (size == null || size < MIN_SIZE) {
            return DEFAULT_SIZE;
        }
        return Math.min(size, MAX_SIZE);
    }
}
