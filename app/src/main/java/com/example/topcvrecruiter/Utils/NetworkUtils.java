package com.example.topcvrecruiter.Utils;

import android.os.Build;

public class NetworkUtils {
    public static String getBaseUrl() {
        if (isEmulator()) {
            return "https://10.0.2.2:7200/";
        } else {
            return "http://localhost:7200/";
        }
    }

    private static boolean isEmulator() {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.BOARD.contains("unknown")
                || Build.SERIAL.equals("Android")
                || Build.SERIAL.equals("unknown"));
    }
}
