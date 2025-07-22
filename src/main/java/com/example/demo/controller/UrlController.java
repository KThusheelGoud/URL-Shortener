package com.example.demo.controller;

import com.example.demo.domain.UrlRequest;
import com.example.demo.domain.UrlResponse;
import com.example.demo.domain.ShortLink;
import com.example.demo.service.UrlService;
import com.example.demo.utils.RemoteLogger;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shorten")
public class UrlController {

    private UrlService urlService = new UrlService();

    @PostMapping
    public ResponseEntity<?> shortenUrl(@RequestBody UrlRequest request) {
        if (request.getOriginalUrl() == null || request.getOriginalUrl().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Original URL is required"));
        }

        RemoteLogger.sendLog("backend", "info", "controller",
                "Shorten request received for URL: " + request.getOriginalUrl());

        ShortLink shortLink = urlService.createShortLink(request.getOriginalUrl());
        String fullShortenedUrl = "http://localhost:8080/" + shortLink.getShortCode();

        return ResponseEntity.ok(new UrlResponse(shortLink.getShortCode(), fullShortenedUrl));
    }

    @PostMapping("/custom")
    public ResponseEntity<?> createCustomShortLink(@RequestBody UrlRequest request) {
        if (request.getUrl() == null || request.getUrl().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "URL is required"));
        }

        if (request.getShortcode() == null || request.getShortcode().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Shortcode is required"));
        }

        if (urlService.getLinkByCode(request.getShortcode()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Shortcode already exists"));
        }

        int validity = request.getValidity();
        if (validity <= 0) {
            validity = 30;
        }

        RemoteLogger.sendLog("backend", "info", "controller",
                "Custom short link requested for: " + request.getUrl());

        UrlResponse response = urlService.createCustomShortLink(request.getUrl(), validity, request.getShortcode());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirectToOriginal(@PathVariable String shortCode) {
        ShortLink link = urlService.getLinkByCode(shortCode);

        if (link == null) {
            RemoteLogger.sendLog("backend", "error", "controller",
                    "Invalid shortCode: " + shortCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Shortcode not found"));
        }

        String expiryStr = link.getExpiry();
        if (expiryStr != null) {
            Instant expiry = Instant.parse(expiryStr);
            if (Instant.now().isAfter(expiry)) {
                RemoteLogger.sendLog("backend", "warn", "controller",
                        "Expired shortCode: " + shortCode);
                return ResponseEntity.status(HttpStatus.GONE)
                        .body(Map.of("error", "Shortcode has expired"));
            }
        }

        urlService.increaseClickCount(shortCode);
        RemoteLogger.sendLog("backend", "info", "controller", "Redirected: " + shortCode);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", link.getOriginalUrl())
                .build();
    }

    @GetMapping("/analytics/{shortCode}")
    public ResponseEntity<?> getAnalytics(@PathVariable String shortCode) {
        ShortLink link = urlService.getLinkByCode(shortCode);

        if (link == null) {
            RemoteLogger.sendLog("backend", "warn", "controller",
                    "Analytics requested for invalid code: " + shortCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Shortcode not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("originalUrl", link.getOriginalUrl());
        response.put("shortCode", link.getShortCode());
        response.put("clickCount", link.getClickCount());
        response.put("expiry", link.getExpiry());

        RemoteLogger.sendLog("backend", "info", "controller",
                "Analytics fetched for: " + shortCode);
        return ResponseEntity.ok(response);
    }
}