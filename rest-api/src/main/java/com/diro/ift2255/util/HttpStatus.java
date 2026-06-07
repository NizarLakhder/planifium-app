package com.diro.ift2255.util;

import java.util.Map;

public class HttpStatus {
    private static final Map<Integer, String> REASONS = Map.ofEntries(
        Map.entry(200, "OK"),
        Map.entry(201, "Created"),
        Map.entry(400, "Bad Request"),
        Map.entry(401, "Unauthorized"),
        Map.entry(403, "Forbidden"),
        Map.entry(404, "Not Found"),
        Map.entry(500, "Internal Server Error")
    );

    public static String reasonPhrase(int code) {
        return REASONS.getOrDefault(code, "Unknown");
    }
}
