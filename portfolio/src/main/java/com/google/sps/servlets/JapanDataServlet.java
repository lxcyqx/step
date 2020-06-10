package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/japan-data")
public class JapanDataServlet extends HttpServlet {

  private LinkedHashMap<String, Integer> populationMap = new LinkedHashMap<>();

  public void getData(Scanner scanner) {
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      String city = String.valueOf(cells[0]);
      if (cells.length > 1){
        Integer population = Integer.valueOf(cells[1]);
        System.out.println(city);
        populationMap.put(city, population);
      }
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String dataType = request.getParameter("dataType");
    Scanner scanner = null;
    if (dataType.equals("population")){
      scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/jp-city-population.csv")); 
    } else if (dataType.equals("lifeExpectancy")){
      scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/jp-life-expectancy.csv")); 
    }
    if (scanner!= null){
      getData(scanner);
    }
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(populationMap);
    response.getWriter().println(json);
  }
}
