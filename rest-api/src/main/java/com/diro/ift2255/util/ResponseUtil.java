package com.diro.ift2255.util;

import java.util.Map;

public class ResponseUtil {
    public static Map<String, String> formatError(String errorMessage) {
        return Map.of("error", errorMessage);
    }
}
