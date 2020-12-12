package com.sprc.weatherapp.api.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private DateUtils() {
        // utils class
    }

    public static boolean isValidFormat(String value) {
        return isValidFormat("yyyy-MM-dd", value);
    }

    public static boolean isValidFormat(String format, String value) {
        if(value != null) {
            try {
                LocalDate.parse(value, DateTimeFormatter.ofPattern(format));
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static LocalDate convertStringToLocalDate(String format, String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(value, formatter);
    }

    public static LocalDate convertStringToLocalDate(String value) {
        return convertStringToLocalDate("yyyy-MM-dd", value);
    }
}
