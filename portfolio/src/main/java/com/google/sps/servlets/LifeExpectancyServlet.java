package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Returns Japan city population as a JSON object, e.g. {"Tokyo": 1000000, "Kyoto": 200000}] */
@WebServlet("/life-expectancy")
public class LifeExpectancyServlet extends HttpServlet {

  /* LinkedHashMap with predictable interation order so larger cities show up on the map first */
  private LinkedHashMap<Integer, Double> lifeExpectancyMap = new LinkedHashMap<>();

  @Override
  public void init() {
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/jp-life-expectancy.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      Integer year = Integer.valueOf(cells[0]);
      if (cells.length > 1){
        Double lifetime = Double.valueOf(cells[1]);
        lifeExpectancyMap.put(year, lifetime);
      }
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(lifeExpectancyMap);
    response.getWriter().println(json);
  }
}
