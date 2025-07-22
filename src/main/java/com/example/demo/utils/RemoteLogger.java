package com.example.demo.utils;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

public class RemoteLogger {

    private static final String LOG_API_URL = "http://20.244.56.144/evaluation-service/logs";

    private static final Set<String> VALID_STACKS = Set.of("frontend", "backend");
    private static final Set<String> VALID_LEVELS = Set.of("debug", "info", "warn", "error", "fatal");

    private static final Set<String> BACKEND_PACKAGES = Set.of(
            "cache", "controller", "cron_job", "db", "domain", "handler", "repository", "route", "service");
    private static final Set<String> FRONTEND_PACKAGES = Set.of(
            "api", "component", "hook", "page", "state", "style");
    private static final Set<String> SHARED_PACKAGES = Set.of(
            "auth", "config", "middleware", "utils");

    public static void sendLog(String stack, String level, String packageName, String message) {
        String stackLC = stack.toLowerCase();
        String levelLC = level.toLowerCase();
        String packageLC = packageName.toLowerCase();

        boolean validStack = VALID_STACKS.contains(stackLC);
        boolean validLevel = VALID_LEVELS.contains(levelLC);
        boolean validPackage = BACKEND_PACKAGES.contains(packageLC)
                || FRONTEND_PACKAGES.contains(packageLC)
                || SHARED_PACKAGES.contains(packageLC);

        if (!validStack || !validLevel || !validPackage) {
            System.err.println("Log rejected Invalid values:");
            System.err.println("stack: " + stack + ", level: " + level + ", package: " + packageName);
            return;
        }

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> payload = new HashMap<>();
        payload.put("stack", stackLC);
        payload.put("level", levelLC);
        payload.put("package", packageLC);
        payload.put("message", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(LOG_API_URL, requestEntity, String.class);
            System.out.println("Log sent Status: " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("Log failed  Reason: " + e.getMessage());
        }
    }
}