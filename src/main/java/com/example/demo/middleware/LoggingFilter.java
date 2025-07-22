package com.example.demo.middleware;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.Enumeration;

import com.example.demo.utils.RemoteLogger;

public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        String method = httpReq.getMethod();
        String uri = httpReq.getRequestURI();
        String timestamp = Instant.now().toString();

        System.out.println("[" + timestamp + "] " + method + " " + uri);

        Enumeration<String> headerNames = httpReq.getHeaderNames();
        System.out.println("Headers:");
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            System.out.println("  " + header + ": " + httpReq.getHeader(header));
        }

        RemoteLogger.sendLog("backend", "info", "com.example.demo.middleware.loggingfilter",
                "Intercepted request: " + method + " " + uri);

        chain.doFilter(request, response);
    }
}