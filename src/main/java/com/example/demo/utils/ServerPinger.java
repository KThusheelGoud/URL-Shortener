package com.example.demo.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ServerPinger {

    public static void logTestPing(String stack, String level, String packageName, String testUrl) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(testUrl, String.class);
            String message = "Ping success: " + response.getStatusCode() + " - " + response.getBody();
            System.out.println("Log(" + stack + ", " + level + ", " + packageName + ", " + message + ")");
        } catch (Exception e) {
            String message = "Ping failed: " + e.getMessage();
            System.out.println("Log(" + stack + ", ERROR, " + packageName + ", " + message + ")");
        }
    }
}