package com.google.sps.servlets;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Returns Japan city population as a JSON object, e.g. {"Tokyo": 1000000,
 * "Kyoto": 200000}].
 *
 * @author lucyqu
 *
 */
@WebServlet("/japan-population")
public class JapanPopulationServlet extends HttpServlet {

  /*
   * LinkedHashMap with predictable interation order so larger cities show up on
   * the map first
   */
  private LinkedHashMap<String, Integer> populationMap = new LinkedHashMap<>();

  @Override
  public void init() {
    Scanner scanner = new Scanner(
        getServletContext().getResourceAsStream("/WEB-INF/jp-city-population.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      String city = String.valueOf(cells[0]);
      if (cells.length > 1) {
        Integer population = Integer.valueOf(cells[1]);
        populationMap.put(city, population);
      }
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(populationMap);
    response.getWriter().println(json);
  }
}
