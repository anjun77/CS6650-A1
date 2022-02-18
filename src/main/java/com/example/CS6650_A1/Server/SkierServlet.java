package com.example.CS6650_A1.Server;

import com.google.gson.Gson;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "SkierServlet", value = "/skiers/*")
public class SkierServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
        // /skiers/{skierID}/vertical
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        String urlPath = request.getPathInfo();

        //check we have a URL
        if (urlPath == null || urlPath.isEmpty()) {
          setStatusAsNotFound(response);
          return;
        }

        String[] urlParts = urlPath.split("/");

        if (isGetUrlValid(urlParts)) {
          setStatusAsValid(response);
          String jsonData;
          if (urlParts.length == 4) {
            jsonData = gson.toJson("77");
          } else {
            jsonData = gson.toJson("7177");
          }
          out.write(jsonData);
        } else {
          setStatusAsInvalid(response);
          return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String urlPath = request.getPathInfo();
        //check we have a URL
        if (urlPath == null || urlPath.isEmpty()) {
          setStatusAsNotFound(response);
          return;
        }

        String[] urlParts = urlPath.split("/");

        if (!isPostUrlValid(urlParts)) {
          setStatusAsInvalid(response);
          return;
        } else {
          setStatusAsCreated(response);
        }

    }

    private void setStatusAsNotFound(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("Not found");
    }

    private void setStatusAsInvalid(HttpServletResponse response) throws IOException {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("Invalid parameter");
    }

    private void setStatusAsValid(HttpServletResponse response) throws IOException {
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write("Success!");
    }

    private void setStatusAsCreated(HttpServletResponse response) throws IOException {
      response.setStatus(HttpServletResponse.SC_CREATED);
      response.getWriter().write("Data stored!");
    }

    private boolean isGetUrlValid(String[] urlParts) {
        // /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
        // /skiers/{skierID}/vertical
        if (urlParts.length == 3) {
          boolean wordsMatch = urlParts[2].equals("vertical");
          boolean intMatch = IntHelper.isInteger(urlParts[1]);
          System.out.println(intMatch);
          return wordsMatch && intMatch;
        } else if (urlParts.length == 8) {
          return handleDayVerticalForASkier(urlParts);
        } else {
          return false;
        }
    }

    private boolean isPostUrlValid(String[] urlParts) {
        // /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
        return handleDayVerticalForASkier(urlParts);
    }

    private boolean handleDayVerticalForASkier(String[] urlParts) {
        // /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
        if (urlParts.length == 8) {
          boolean wordsMatch = (urlParts[2].equals("seasons")
              && urlParts[4].equals("days") && urlParts[6].equals("skiers"));
          boolean intMatch = IntHelper.isInteger(urlParts[1]) && IntHelper.isInteger(urlParts[3])
              && IntHelper.isInteger(urlParts[5]) && IntHelper.isInteger(urlParts[7]);
          return wordsMatch && intMatch;
        } else {
          return false;
        }
    }
}
