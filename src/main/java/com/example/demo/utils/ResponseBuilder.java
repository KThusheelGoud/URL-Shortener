package com.example.demo.utils;

import java.util.Map;
import java.util.HashMap;

public class ResponseBuilder {
    public static Map<String, Object> error(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", message);
        return body;
    }
}