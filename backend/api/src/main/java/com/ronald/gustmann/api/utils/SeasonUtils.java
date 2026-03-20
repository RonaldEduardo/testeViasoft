package com.ronald.gustmann.api.utils;

import java.time.LocalDate;

public class SeasonUtils {
    public static String getSeason(LocalDate date) {
        int m = date.getMonthValue();
        int d = date.getDayOfMonth();

        if (isBetween(m, d, 12, 21, 3, 20)) return "VERAO";
        if (isBetween(m, d, 3, 21, 6, 20)) return "OUTONO";
        if (isBetween(m, d, 6, 21, 9, 22)) return "INVERNO";
        return "PRIMAVERA";
    }

    private static boolean isBetween(int m, int d, int startM, int startD, int endM, int endD) {
        int value = m * 100 + d;
        int start = startM * 100 + startD;
        int end = endM * 100 + endD;

        if (start <= end) {
            return value >= start && value <= end;
        }
        return value >= start || value <= end;
    }
}
