package com.example.demo.service;

import com.example.demo.domain.ShortLink;
import com.example.demo.domain.UrlResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class UrlService {

    private Map<String, ShortLink> shortLinkStorage = new HashMap<>();

    public ShortLink createShortLink(String originalUrl) {
        String shortCode = generateUniqueCode();
        ShortLink link = new ShortLink(originalUrl, shortCode, 0L);
        shortLinkStorage.put(shortCode, link);
        return link;
    }

    public UrlResponse createCustomShortLink(String url, int validityMinutes, String shortcode) {
        Instant expiryTime = Instant.now().plus(validityMinutes, ChronoUnit.MINUTES);
        ShortLink link = new ShortLink(url, shortcode, 0L);
        link.setExpiry(expiryTime.toString());
        shortLinkStorage.put(shortcode, link);
        String fullShortUrl = "http://localhost:8080/" + shortcode;
        return new UrlResponse(fullShortUrl, expiryTime.toString());
    }

    public ShortLink getLinkByCode(String code) {
        return shortLinkStorage.get(code);
    }

    public void increaseClickCount(String code) {
        ShortLink link = shortLinkStorage.get(code);
        if (link != null) {
            link.setClickCount(link.getClickCount() + 1);
        }
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}