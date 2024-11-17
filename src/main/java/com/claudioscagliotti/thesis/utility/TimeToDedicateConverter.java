package com.claudioscagliotti.thesis.utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for converting hours dedicated to a task into the corresponding number of days.
 */
public class TimeToDedicateConverter {
    private TimeToDedicateConverter() {
        throw new UnsupportedOperationException("Utility class");
    }
    private static final Map<Float, Integer> hoursToDaysMap = new HashMap<>();

    static {
        hoursToDaysMap.put(2.5f, 7);  // 2.5 hours -> 7 days
        hoursToDaysMap.put(5.0f, 6);  // 5.0 hours -> 6 days
        hoursToDaysMap.put(7.5f, 5);  // 7.5 hours -> 5 days
        hoursToDaysMap.put(10.0f, 4); // 10.0 hours -> 4 days
        hoursToDaysMap.put(12.5f, 3); // 12.5 hours -> 3 days
        hoursToDaysMap.put(15.0f, 1); // 15.0 hours -> 1 day
    }

    /**
     * Converts the given time dedicated in hours to the corresponding number of days.
     *
     * @param timeToDedicate The time dedicated in hours.
     * @return The corresponding number of days.
     * @throws IllegalArgumentException If the timeToDedicate is not one of the predefined values (2.5, 5.0, 7.5, 10.0, 12.5, 15.0 hours).
     */
    public static int convertTimeToDedicateToDays(float timeToDedicate) {
        Integer days = hoursToDaysMap.get(timeToDedicate);

        if (days == null) {
            throw new IllegalArgumentException("Invalid value for timeToDedicate. It must be one of the following: 2.5, 5.0, 7.5, 10.0, 12.5, 15.0 hours.");
        }

        return days;
    }
}

