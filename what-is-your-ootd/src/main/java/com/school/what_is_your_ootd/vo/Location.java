package com.school.what_is_your_ootd.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
    private double latitude;
    private double longitude;
    // 대한민국
    private static final int LAT_MIN = 33;
    private static final int LAT_MAX = 38;
    private static final int LNG_MIN = 125;
    private static final int LNG_MAX = 132;

    public static boolean validateRange(Location location) {
        if (LAT_MIN <= location.latitude && location.latitude <= LAT_MAX
                && LNG_MIN <= location.longitude && location.longitude <= LNG_MAX) {
            return true;
        }

        return false;
    }
}