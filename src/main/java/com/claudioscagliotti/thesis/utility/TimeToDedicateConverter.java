package com.claudioscagliotti.thesis.utility;

import java.util.HashMap;
import java.util.Map;

public class TimeToDedicateConverter {
    private static final Map<Float, Integer> hoursToDaysMap = new HashMap<>();

    static {
        hoursToDaysMap.put(2.5f, 7);  // 2.5 ore -> 7 giorni
        hoursToDaysMap.put(5.0f, 6);  // 5.0 ore -> 6 giorni
        hoursToDaysMap.put(7.5f, 5);  // 7.5 ore -> 5 giorni
        hoursToDaysMap.put(10.0f, 4); // 10.0 ore -> 4 giorni
        hoursToDaysMap.put(12.5f, 3); // 12.5 ore -> 3 giorni
        hoursToDaysMap.put(15.0f, 1); // 15.0 ore -> 1 giorno
    }
    public static int convertTimeToDedicateToDays(float timeToDedicate) {
        Integer days = hoursToDaysMap.get(timeToDedicate);

        if (days == null) {
            throw new IllegalArgumentException("Valore di timeToDedicate non valido. Deve essere uno tra 2.5, 5.0, 7.5, 10.0, 12.5, 15.0 ore.");
        }

        return days;
    }
}
