package com.sprc.weatherapp.api.utils;

public class TypeUtils {

    private TypeUtils() {
        // utils class
    }

    public static boolean isValidDouble(String value) {
        if (value != null) {
            try {
                Double.parseDouble(value);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean isValidLong(String value) {
        if (value != null) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
