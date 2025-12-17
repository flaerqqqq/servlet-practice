package org.author.demo.servletpractice.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

// filter is described in web.xml
public class AuthFilter implements Filter {

    private static final String AUTH_TOKEN_HEADER_VALUE = "x-auth-token";
    private static final String AUTH_TOKEN_REF = "admin-secret";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String headerValue = req.getHeader(AUTH_TOKEN_HEADER_VALUE);

        if (headerValue != null && Objects.equals(headerValue, AUTH_TOKEN_REF)) {
            req.setAttribute("user_role", "ADMIN");
            req.setAttribute("request_id", System.currentTimeMillis());
            chain.doFilter(request, response);
            return;
        }
        res.setStatus(401);
        res.setContentType("application/json"); // or .setHeader("Content-Type", "application/json");
        res.getWriter().print("{\"error\": \"Unauthorized access\"}");
    }
}