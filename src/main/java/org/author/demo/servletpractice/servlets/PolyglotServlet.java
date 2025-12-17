package org.author.demo.servletpractice.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/api/webhook")
public class PolyglotServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String userRole = (String) req.getAttribute("user_role");
        Long requestId = (Long) req.getAttribute("request_id");

        String contentTypeHeaderValue = req.getHeader("content-type");

        if (contentTypeHeaderValue != null) {
            boolean isJson = contentTypeHeaderValue.contains("application/json");
            boolean isBodyForm = contentTypeHeaderValue.contains("application/x-www-form-urlencoded");

            if (isJson) {
                String jsonInputBody = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                JsonNode node = objectMapper.readTree(jsonInputBody);
                if (node.has("id")) {
                    long id = node.get("id").asLong();
                    res.setStatus(200);
                    res.getWriter().print("Processed ID: " + id + " (Role:" + userRole);
                } else {
                    res.setStatus(400);
                    res.setHeader("Content-Type", "application/json");
                    res.getWriter().print("{\"error\": \"Invalid ID\"}");
                }
            } else if (isBodyForm) {
                String idValue = req.getParameter("id");
                res.setStatus(200);
                res.getWriter().print("Processed ID: " + idValue + " (Role:" + userRole);
            }
        }
    }
}