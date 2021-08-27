package com.example.generactive_web2.servlet;



import com.example.generactive_web2.model.BasketItem;
import com.example.generactive_web2.model.Item;
import com.example.generactive_web2.model.StockItem;
import com.example.generactive_web2.servlet.util.ErrorEntity;
import com.example.generactive_web2.servlet.util.HttpConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "itemsServlet", value = "/items-servlet")
public class ItemsServlet extends HttpServlet {
    private final List<Item> items = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getContextPath());
        System.out.println(req.getMethod());
        System.out.println(req.getPathInfo());
        System.out.println(req.getPathTranslated());
        System.out.println(req.getRequestURI());
        System.out.println(req.getServletPath());
        System.out.println(req.getRemoteAddr());
        System.out.println(req.getRemoteHost());
        System.out.println(req.getQueryString());
        System.out.println(req.getServerName());
        resp.setContentType(HttpConstants.ContentType.APPLICATION_JSON);

        ObjectMapper objectMapper;
        objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(items);
        PrintWriter writer = resp.getWriter();
        writer.write(response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ObjectMapper parse Java objects to JSON and vise a versa.
        ObjectMapper objectMapper = new ObjectMapper();
        // accept only application/json
        if (!req.getContentType().equals(HttpConstants.ContentType.APPLICATION_JSON)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "not_supported_format");
        }

        // Read the body content
        BufferedReader bufferedReader = req.getReader();
        String body = bufferedReader.lines().collect(Collectors.joining());

        try {
            StockItem item = objectMapper.readValue(body, StockItem.class);
            items.add(item);
            resp.getWriter().write(objectMapper.writeValueAsString(item));
        } catch (RuntimeException e) {
            ErrorEntity errorEntity = new ErrorEntity("json_parse_failed:" + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(errorEntity));
        }
    }

    protected String getItemAsJson(Item item) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(item);
    }
}

