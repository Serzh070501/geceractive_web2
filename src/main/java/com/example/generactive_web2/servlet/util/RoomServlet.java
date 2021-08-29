package com.example.generactive_web2.servlet.util;


import javax.servlet.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * Illustrates {@link GenericServlet} usage.
 * Servlet is defined in {@code web.xml} file.
 */
public class RoomServlet extends GenericServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        String scheme = req.getScheme();
        System.out.println("scheme: " + scheme);

        long contentLength = req.getContentLength();
        System.out.println("content length: " + contentLength);

        // Get all params with values
        System.out.println("Params:");
        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            System.out.println(paramName + " : " + req.getParameter(paramName));
        }

        // Read payload from InputStream
        ServletInputStream servletInputStream = req.getInputStream();
        String requestPayload = readInput(servletInputStream);
        System.out.println(requestPayload);

        // Write into an OutputStream
        ServletOutputStream servletOutputStream = res.getOutputStream();
        servletOutputStream.write("This is our response".getBytes());
    }

    private String readInput(InputStream inputStream) throws IOException {
        // ByteArrayOutputStream and read shows best performance in almost all cases
        // and machines
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = inputStream.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}
