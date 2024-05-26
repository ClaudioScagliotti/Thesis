package com.claudioscagliotti.thesis.enumeration.tmdb;


import com.claudioscagliotti.thesis.interfaces.tmdb.AppendToResponseMethod;
import io.micrometer.common.util.StringUtils;

/**
 * List of all "Append To Response" movie methods
 *
 * @author Stuart
 */
public enum MovieMethod implements AppendToResponseMethod {

    ALTERNATIVE_TITLES,
    CHANGES,
    CREDITS,
    IMAGES,
    KEYWORDS,
    LISTS,
    RECOMMENDATIONS,
    RELEASES,
    REVIEWS,
    SIMILAR,
    TRANSLATIONS,
    VIDEOS;

    /**
     * Get the string to use in the URL
     *
     * @return the string representation of the enum
     */
    @Override
    public String getPropertyString() {
        return this.name().toLowerCase();
    }

    /**
     * Convert a string into an Enum type
     *
     * @param method String to convert to enum
     * @return enum version of param
     * @throws IllegalArgumentException If type is not recognised
     *
     */
    public static MovieMethod fromString(String method) {
        if (StringUtils.isNotBlank(method)) {
            try {
                return MovieMethod.valueOf(method.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Method " + method + " does not exist.", ex);
            }
        }
        throw new IllegalArgumentException("Method must not be null");
    }
}
