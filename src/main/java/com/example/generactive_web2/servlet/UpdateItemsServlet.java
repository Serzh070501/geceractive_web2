package com.example.generactive_web2.servlet;

import com.example.generactive_web2.model.Item;
import com.example.generactive_web2.model.StockItem;
import com.example.generactive_web2.servlet.util.ErrorEntity;
import com.example.generactive_web2.servlet.util.HttpConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(name = "updateitemsServlet", value = "/items-update")
public class UpdateItemsServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(HttpConstants.ContentType.APPLICATION_JSON);
        PrintWriter out = resp.getWriter ();
        String sid = req.getParameter ("id");
        int id = Integer.parseInt (sid);
        String name = req.getParameter ("name");
        String dept = req.getParameter ("dept");
        for (int i = 0; i < ItemsServlet.items.size(); i++) {
            if(ItemsServlet.items.get(i).getId() == id){
                ItemsServlet.items.remove(i);

            }

        }
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
            ItemsServlet.items.add(item);
            resp.getWriter().write(objectMapper.writeValueAsString(item));
        } catch (RuntimeException e) {
            ErrorEntity errorEntity = new ErrorEntity("json_parse_failed:" + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(errorEntity));
        }

    }
}
