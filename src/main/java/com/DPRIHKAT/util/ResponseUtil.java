package com.DPRIHKAT.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    public static Map<String, Object> createMeta() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        meta.put("version", "1.0.0");
        return meta;
    }

    public static Map<String, Object> createSuccessResponse(Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        response.put("error", null);
        response.put("meta", createMeta());
        return response;
    }

    public static Map<String, Object> createErrorResponse(String code, String message, String details) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", code);
        error.put("message", message);
        error.put("details", details);

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("data", null);
        response.put("error", error);
        response.put("meta", createMeta());
        return response;
    }
}
