package am.aca.generactive.servlets;

import am.aca.generactive.model.GenerativeItem;
import am.aca.generactive.model.Item;
import am.aca.generactive.model.StockItem;
import am.aca.generactive.repository.ItemRepository;
import am.aca.generactive.servlets.enums.ItemType;
import am.aca.generactive.util.URLUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Optional;
import java.util.stream.Collectors;

import static am.aca.generactive.servlets.HttpConstants.CONTENT_TYPE_JSON;

@WebServlet(name = "ItemServlet", urlPatterns = "/item/*")
public class ItemServlet extends HttpServlet {

    public static final String PARAM_TYPE = "type";
    private final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String USER = "postgres";
    private final String PASSWORD = "123456";
    private final ItemRepository itemRepository = ItemRepository.getInstance();

    /**
     * Receive {@link Item} object in JSON format String.
     * Expected url param(s): {@code type}, must be one of {@link ItemType}.
     * Respond with updated {@link Item} and status code {@code 200}.
     * Respond with error message and status code {@code 400}, if missing required
     * param(s), wrong {@link ItemType} string representation,
     * code {@code 404} if item not found.
     *
     * Updated Item will be updated in {@link ItemRepository}
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String typeParam = req.getParameter(PARAM_TYPE);
        resp.setContentType(CONTENT_TYPE_JSON);
        if (typeParam == null || typeParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing param: " + PARAM_TYPE);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        ItemType itemType = ItemType.valueOf(typeParam);

        String payload = req.getReader().lines().collect(Collectors.joining());
        Item item;
        switch (itemType) {
            case GENERATIVE:
                item = objectMapper.readValue(payload, GenerativeItem.class);
                break;
            case STOCK:
                item = objectMapper.readValue(payload, StockItem.class);
                break;
            default:
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Wrong type: " + itemType);
                return;
        }

        Integer itemId = URLUtils.getLastPathSegment(req, resp);
        if (itemId == null) return;

        item.setId(itemId);
        if (itemRepository.updateItem(item) == 0) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Resource not found: " + itemId);
        }

        resp.getWriter().write(objectMapper.writeValueAsString(item));
    }

    /**
     * Get all items
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from item ");
            ResultSet resultSet = preparedStatement.executeQuery();
            ObjectMapper objMapper = new ObjectMapper();
            resp.getWriter().write(objMapper.writeValueAsString(resultSet));

            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("delete * from item where id = ?");
            ResultSet resultSet = preparedStatement.executeQuery();
            ObjectMapper objMapper = new ObjectMapper();
            resp.getWriter().write(objMapper.writeValueAsString(resultSet));

            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Resource not found: " );

    }
}
