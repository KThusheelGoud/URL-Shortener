package com.example.demo.domain;

public class UrlResponse {

    private String shortLink;
    private String expiry;

    public UrlResponse() {
    }

    public UrlResponse(String shortLink, String expiry) {
        this.shortLink = shortLink;
        this.expiry = expiry;
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}