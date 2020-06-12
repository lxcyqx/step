/**
 * Initializes by loading the charts.
 */
function init() {
  google.charts.load("current", {
    packages: ["geochart"],
    mapsApiKey: "AIzaSyC35R1Q1qkQndHm-Ni6isXLbsSCQif3Umg"
  });
  google.charts.setOnLoadCallback(getChart);
  google.charts.load("current", { packages: ["corechart"] });
}

/**
 * Gets the appropriate chart given the user selection from the dropdown.
 */
function getChart() {
  const chartType = document.getElementById("chart-type").value;
  if (chartType === "GeoMap") {
    drawMarkersMap();
  } else if (chartType === "LineChart") {
    drawChart();
  }
}

/**
 * Create map that includes Japan's city and population information using GeoMap, which makes use of Google Charts API,
 * Maps API, and Geocoding API.
 */
function drawMarkersMap() {
  fetch("/japan-population")
    .then(response => response.json())
    .then(cityPopulations => {
      const data = new google.visualization.DataTable();
      data.addColumn("string", "City");
      data.addColumn("number", "Population");
      Object.keys(cityPopulations).forEach(city => {
        data.addRow([city, cityPopulations[city]]);
      });

      const options = {
        title: "Population of Japan's Largest Cities",
        region: "JP",
        displayMode: "markers",
        colorAxis: { colors: ["blue", "red"] }
      };

      const geomap = new google.visualization.GeoChart(
        document.getElementById("chart")
      );
      geomap.draw(data, options);
    });
}

/**
 * Draws line chart containing information on Japan life expectancy by year.
 */
function drawChart() {
  fetch("/life-expectancy")
    .then(response => response.json())
    .then(lifeExpectancy => {
      const data = new google.visualization.DataTable();
      data.addColumn("string", "Year");
      data.addColumn("number", "Life Expectancy");
      Object.keys(lifeExpectancy).forEach(year => {
        data.addRow([year, lifeExpectancy[year]]);
      });
      const options = {
        title: "Japan Life Expectancy",
        legend: { position: "bottom" }
      };

      const lineChart = new google.visualization.LineChart(
        document.getElementById("chart")
      );

      lineChart.draw(data, options);
    });
}

init();
