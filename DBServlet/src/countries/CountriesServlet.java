package countries;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CountriesServlet
 */
@WebServlet("/CountriesServlet")
public class CountriesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

	public static final String FORM_CITY_ID = "CityId";
	public static final String FORM_CITY_NAME = "CityName";
	public static final String FORM_CITY_COUNTRY_CODE = "CityCountryCode";
	public static final String FORM_CITY_POPULATION = "CityPopulation";
	
	public static final String FORM_COUNTRY_CODE = "CountryCode";
	public static final String FORM_COUNTRY_NAME = "CountryName";
	public static final String FORM_COUNTRY_CONTINENT = "CountryContinent";
	public static final String FORM_COUNTRY_POPULATION = "CountryPopulation";
	public static final String FORM_COUNTRY_LIFE_EXPECTANCY = "CountryLifeExpectancy";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CountriesServlet() {
		super();

		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e1) {
			System.out.println("******************cannot load da driver");
			e1.printStackTrace();
		}

		this.countriesDAO = new CountriesDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		response.setContentType("text/html");
		out.println("<html><body>");

		System.out
				.println("----------------------- GET BEGIN ----------------");

		if (isListAllCountriesRequest(request)) {

			System.out.println("isGetAllCountriesRequest: " + isListAllCountriesRequest(request));
			this.printHtmlFormForNewCountry(out);

			this.printHtmlFormForFilterByCountryName(request, out);
			
			List<Country> countries = this.getAllCountries();
			this.printHtmlTableForCountries(countries, out);
			
		}

		else if (isFilterCountriesRequest(request)) {

			this.printHtmlFormForFilterByCountryName(request, out);

			String countryName = request.getParameter(FORM_COUNTRY_NAME);
			System.out.println("isFilterCountriesRequest: " + countryName);
			List<Country> countries = this.getCountriesByName(countryName);
			this.printHtmlTableForCountries(countries, out);

			this.printHtmlLinkForAllCountries(out);
			
		}

		else if (isGetCountryRequest(request)) {

			String code = request.getParameter(FORM_COUNTRY_CODE);
			System.out.println("isGetCountryRequest: countryCode " + code);

			Country country = this.getCountryByCode(code);

			this.printHtmlUpdateFormForCountry(country, out);
			this.printHtmlDeleteFormForCountry(country, out);

			List<City> cities = this.getCitiesByCountryCode(code);
			this.printHtmlTableOfCitiesPerCountry(cities, out);

			this.printHtmlFormForNewCity(code, out);

			this.printHtmlLinkForAllCountries(out);
		}

		else if (isGetCityRequest(request)) {
			String cityId = request.getParameter(FORM_CITY_ID);
			System.out.println("isGetCityRequest: " + isGetCityRequest(request) + ", with cityId=" + cityId);
			City city = this.getCityByID(cityId);

			this.printHtmlUpdateFormForCity(city, out);
			this.printHtmlDeleteFormForCity(city, out);

			String countryCode = city.getCountryCode();
			this.printHtmlLinkForCountry(countryCode, out);
		}

		System.out.println("----------------------- GET DONE ----------------");
		out.println("<p/>");
		out.println("<hr/>");
		out.println("Servlet invoked on this date " + new Date());

		out.println("</body></html>");

	}

	
	private void printHtmlLinkForCountry(String code, PrintWriter out) {
		String urlForCountry = "http://localhost:8080/DBServlet/CountriesServlet?CountryCode="
				+ code;
		out.println();
		out.println("<a href=\"" + urlForCountry + "\" >Country</a>");
	}

	private boolean isFilterCountriesRequest(HttpServletRequest request) {
		String countryName = request.getParameter(FORM_COUNTRY_NAME);
		return (countryName != null);
	}

	/**
	 * @param out
	 */
	private void printHtmlLinkForAllCountries(PrintWriter out) {
		String urlForAllCountries = "http://localhost:8080/DBServlet/CountriesServlet";
		out.println();
		out.println("<a href=\"" + urlForAllCountries + "\" >All Countries</a>");
	}

	
	/**
	 * @param name
	 * @param out
	 */
	private void printHtmlFormForFilterByCountryName(HttpServletRequest request,
			PrintWriter out) {
		String url = "http://localhost:8080/DBServlet/CountriesServlet";

		String countryName = request.getParameter(FORM_COUNTRY_NAME);

		if (countryName == null) {
			countryName = "";
		}

		out.print("<hr/>");
		out.println("<h3>Filter by Country Name: " + countryName + "</h3>");

		out.print("<td><a href=\"" + url + "\" ></a></td>");
		out.println("<form action = \"" + url + "\" >");
		out.println("<input type=\"text\" name=\"" + FORM_COUNTRY_NAME +"\" value=\"" + countryName
				+ "\" >");
		out.println("<input type=\"submit\" value=\"Submit\">");
		out.println("</form>");
	}

	private boolean isListAllCountriesRequest(HttpServletRequest request) {
		String countryCode = request.getParameter(FORM_COUNTRY_CODE);
		String countryName = request.getParameter(FORM_COUNTRY_NAME);
		String cityId = request.getParameter(FORM_CITY_ID);

		return ((countryCode == null) && (countryName == null) && (cityId == null));
	}

	private boolean isDeleteCountryRequest(HttpServletRequest request) {
		String countryCode = request.getParameter(FORM_COUNTRY_CODE);
		String countryName = request.getParameter(FORM_COUNTRY_NAME);
		boolean isPostRequest = request.getMethod().equalsIgnoreCase("POST");

		return (isPostRequest && (countryCode != null) && (countryName == null));
	}

	private boolean isGetCountryRequest(HttpServletRequest request) {
		String code = request.getParameter(FORM_COUNTRY_CODE);
		return (code != null);
	}
	
		private boolean isGetCityRequest(HttpServletRequest request) {
		String cityId = request.getParameter(FORM_CITY_ID);
		String cityName = request.getParameter(FORM_CITY_NAME);

		boolean isGetRequest = request.getMethod().equalsIgnoreCase("GET");

		return (isGetRequest && (cityId != null) && (cityName == null));
	}

	private boolean isUpdateCityRequest(HttpServletRequest request) {
		String cityId = request.getParameter(FORM_CITY_ID);
		String cityCountryCode= request.getParameter(FORM_CITY_COUNTRY_CODE);
		return ((cityId != null) && (cityCountryCode != null)) ;
	}

	private boolean isDeleteCityRequest(HttpServletRequest request) {
		String cityId = request.getParameter(FORM_CITY_ID);
		String cityName = request.getParameter(FORM_CITY_NAME);		

		boolean isPostRequest = request.getMethod().equalsIgnoreCase("POST");

		return (isPostRequest && (cityId != null))	&& (cityName == null);
	}

	
	private City getCityByID(String cityId) {
		return this.countriesDAO.getCityByID(cityId);
	}

	private List<Country> getCountriesByName(String countryName) {
		return this.countriesDAO.getCountriesByName(countryName);
	}

	private CountriesDAO countriesDAO;

	private Country getCountryByCode(String code) {
		return this.countriesDAO.getCountryByCode(code);
	}

	private List<Country> getAllCountries() {
		return this.countriesDAO.getAllCountries();
	}

	private List<City> getCitiesByCountryCode(String countryCode) {
		return this.countriesDAO.getCitiesByCountryCode(countryCode);
	}

	private void printHtmlUpdateFormForCountry(Country country, PrintWriter out) {

		out.println("<br/>");
		String urlForCountries = "http://localhost:8080/DBServlet/CountriesServlet";

		Date date = new Date();
		out.println("<h2>Country as of " + date + "</h2>");

		out.println("<form action =\"" + urlForCountries
				+ "\" method = \"post\" >");
		out.println("Country code: <input type=\"text\" name=\"" + FORM_COUNTRY_CODE + "\" value=\""
				+ country.getCode() + "\" readonly />");
		out.println("<br>");
		out.println("Country name: <input type=\"text\" name=\"" + FORM_COUNTRY_NAME + "\" value=\""
				+ country.getName() + "\" />");
		out.println("<br>");
		out.println("Continent: <input type=\"text\" name=\"" + FORM_COUNTRY_CONTINENT + "\" value=\""
				+ country.getContinent() + "\" />");
		out.println("<br>");
		out.println("Population: <input type=\"text\" name=\"" + FORM_COUNTRY_POPULATION + "\" value=\""
				+ country.getPopulation() + "\" />");
		out.println("<br>");
		out.println("Life Expectancy: <input type=\"text\" name=\"" + FORM_COUNTRY_LIFE_EXPECTANCY + "\" value=\""
				+ country.getLifeExpectancy() + "\" />");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<input type=\"submit\" value=\"Update\">");
		out.println("<br/>");
		out.println("</form>");

	}

	private void printHtmlDeleteFormForCountry(Country country, PrintWriter out) {
		out.println("<br/>");
		String urlForCountries = "http://localhost:8080/DBServlet/CountriesServlet";
		out.println("<form action =\"" + urlForCountries
				+ "\" method = \"post\" >");
		out.println("<input type=\"hidden\" name=\"" + FORM_COUNTRY_CODE + "\" value=\""
				+ country.getCode() + "\" readonly />");
		out.println("<input type=\"submit\" value=\"Delete\">");
		out.println("</form>");

	}

	private void printHtmlTableOfCitiesPerCountry(List<City> cities,
			PrintWriter out) {
		out.println("<table border=\"1\">");

		out.println("<tr>");
		out.println("<th>ID</th>");
		out.println("<th>Name</th>");

		// out.println("<th>CountryCode</th>");
		// out.println("<th>Population</th>");
		out.println("</tr>");

		for (City city : cities) {
			this.printHtmlTableRowForCity(city, out);
		}
		out.println("</table>");
		out.println("<br>");
		out.println("</br>");

	}

	private void printHtmlTableRowForCity(City city, PrintWriter out) {

		out.println("<tr>");
		out.println("<td>" + city.getID() + "</td>");
		String city_url = "http://localhost:8080/DBServlet/CountriesServlet?CityId="
				+ city.getID();

		out.println("<td><a href=\"" + city_url + "\" >" + city.getName()
				+ "</a></td>");
		// out.println("<td>" + city.getCountryCode() + "</td>");
		// out.println("<td>" + city.getPopulation() + "</td>");
		out.println("</tr>");

	}

	private void printHtmlUpdateFormForCity(City city, PrintWriter out) {
		out.println("<br/>");
		String url = "http://localhost:8080/DBServlet/CountriesServlet";
		out.println("<form action =\"" + url + "\" method = \"post\" >");
		out.println("ID: <input type=\"text\" name=\"" + FORM_CITY_ID + "\" value=\""
				+ city.getID() + "\" readonly />");
		out.println("<br>");
		out.println("Name: <input type=\"text\" name=\"" + FORM_CITY_NAME + "\" value=\""
				+ city.getName() + "\" />");
		out.println("<br>");
		out.println("Country Code: <input type=\"text\" name=\"" + FORM_CITY_COUNTRY_CODE + "\" value=\""
				+ city.getCountryCode() + "\" />");
		out.println("<br>");
		out.println("Population: <input type=\"text\" name=\"" + FORM_CITY_POPULATION + "\" value=\""
				+ city.getPopulation() + "\" />");
		out.println("<br>");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<input type=\"submit\" value=\"Update\">");
		out.println("<br/>");
		out.println("</form>");
	}

	private void printHtmlDeleteFormForCity(City city, PrintWriter out) {
		out.println("<br/>");
		String urlForCities = "http://localhost:8080/DBServlet/CountriesServlet";
		out.println("<form action =\"" + urlForCities
				+ "\" method = \"post\" >");
		out.println("<input type=\"hidden\" name=\"" + FORM_CITY_ID + "\" value=\""
				+ city.getID() + "\" readonly />");	
		out.println("<input type=\"hidden\" name=\"" + FORM_CITY_COUNTRY_CODE + "\" value=\""
				+ city.getCountryCode() + "\" readonly />");	
		
		out.println("<input type=\"submit\" value=\"Delete\">");
		out.println("</form>");

	}

	private void printHtmlFormForNewCity(String cityCountryCode, PrintWriter out) {

		out.println("<br/>");
		out.println("<h3>Enter new city</h3>");
		String urlForCities = "http://localhost:8080/DBServlet/CountriesServlet";
		out.println("<form action =\"" + urlForCities
				+ "\" method = \"post\" >");
		out.println("City Name: <input type=\"text\" name=\"" + FORM_CITY_NAME + "\" value=\"\" />");
		out.println("<br>");
		out.println("Country Code: <input type=\"text\" name=\"" + FORM_CITY_COUNTRY_CODE + "\" value=\""
				+ cityCountryCode + "\" readonly />");
		out.println("<br>");
		out.println("Population: <input type=\"text\" name=\"" + FORM_CITY_POPULATION + "\" value=\"\" />");
		out.println("<br/>");
		out.println("<br/>");
		out.println("<input type=\"submit\" value=\"Submit\">");
		out.println("</form>");
		out.println("<P/>");
	}

	/*private void printHtmlTableForCountry(Country country, PrintWriter out) {
		out.println("<table border=\"1\">");

		out.println("<tr>");
		out.println("<th>Name</th>");
		out.println("<th>Continent</th>");
		out.println("<th>Population</th>");
		out.println("<th>LifeExpectancy</th>");
		out.println("</tr>");
		out.print("</table>");
		this.printHtmlTableRowForCountry(country, out);
	}
	*/

	private void printHtmlFormForNewCountry(PrintWriter out) {

		out.println("<br/>");
		out.println("<br/>");
		out.println("<h3>Enter new country</h3>");
		String urlForCountries = "http://localhost:8080/DBServlet/CountriesServlet";
		out.println("<form action =\"" + urlForCountries
				+ "\" method = \"post\" >");
		out.println("Country code: <input type=\"text\" name=\"" + FORM_COUNTRY_CODE + "\" value=\"\" />");
		out.println("<br>");
		out.println("Country name: <input type=\"text\" name=\"" + FORM_COUNTRY_NAME + "\" value=\"\" />");
		out.println("<br>");
		out.println("Continent: <input type=\"text\" name=\"" + FORM_COUNTRY_CONTINENT + "\" value=\"\" />");
		out.println("<br>");
		out.println("Population: <input type=\"text\" name=\"" + FORM_COUNTRY_POPULATION + "\" value=\"\" />");
		out.println("<br>");
		out.println("Life Expectancy: <input type=\"text\" name=\"" + FORM_COUNTRY_LIFE_EXPECTANCY + "\" value=\"\" />");
		out.println("<br>");
		out.println("<input type=\"submit\" value=\"Submit\">");
		out.println("</form>");
		out.println("<P/>");
	}

	private void printHtmlTableForCountries(List<Country> countries,
			PrintWriter out) {

		out.print("<hr/>");
		Date date = new Date();
		out.println("<h2>Countries as of " + date + "</h2>");

		out.println("<table border=\"1\">");

		out.println("<tr>");
		out.println("<th>Code</th>");
		out.println("<th>Name</th>");
		out.println("<th>Continent</th>");
		out.println("<th>Population</th>");
		out.println("<th>LifeExpectancy</th>");
		out.println("</tr>");

		for (Country country : countries) {
			this.printHtmlTableRowForCountry(country, out);
		}
		out.println("</table>");
	}

	private void printHtmlTableRowForCountry(Country country, PrintWriter out) {
		out.println("<tr>");

		String country_url = "http://localhost:8080/DBServlet/CountriesServlet?CountryCode="
				+ country.getCode();

		out.println("<td><a href=\"" + country_url + "\" >" + country.getCode()
				+ "</a></td>");
		out.println("<td>" + country.getName() + "</td>");
		out.println("<td>" + country.getContinent() + "</td>");
		out.println("<td>" + country.getPopulation() + "</td>");
		out.println("<td>" + country.getLifeExpectancy() + "</td>");
		out.println("</tr>");
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		System.out
				.println("----------------------- POST BEGIN ----------------");

		Map<String, String[]> request_parameters_map = request.getParameterMap();
		System.out.println("Request Parameters: ");
		for (String param_name : request_parameters_map.keySet()) {
			String[] param_value = request_parameters_map.get(param_name);
			System.out.println(" - Name=\"" + param_name + "\", Value=\"" + Arrays.toString(param_value) + "\"");
		}
		System.out.println();
		
		// --------------------------------------------------------
		// parse request parameters for country
		// --------------------------------------------------------
		String countryCode = request.getParameter(FORM_COUNTRY_CODE);
		String countryName = request.getParameter(FORM_COUNTRY_NAME);
		String continent = request.getParameter(FORM_COUNTRY_CONTINENT);
		String countryPopulation = request.getParameter(FORM_COUNTRY_POPULATION);
		String lifeExpectancy = request.getParameter(FORM_COUNTRY_LIFE_EXPECTANCY);
		// --------------------------------------------------------

		boolean isCountryRequest = (countryCode != null);
		System.out.println("isCountryRequest: " + isCountryRequest);

		// --------------------------------------------------------
		// parse request parameters for city
		// --------------------------------------------------------
		String cityId = request.getParameter(FORM_CITY_ID);
		String cityCountryCode = request.getParameter(FORM_CITY_COUNTRY_CODE);
		
		boolean isCityRequest = ((cityId != null) || (cityCountryCode != null));
		System.out.println("isCityRequest: " + isCityRequest + ", with: cityId="
				+ cityId + ", cityCountryCode=" + cityCountryCode);

		if (isCountryRequest) {

			// --------------------------------------------------------
			// check if the request is to delete country or not
			// --------------------------------------------------------
			boolean isDeleteCountryRequest = this.isDeleteCountryRequest(request);

			// --------------------------------------------------------
			// check if country exists for country code
			// --------------------------------------------------------
			boolean isExistingCountry;
			if (countriesDAO.getCountryByCode(countryCode) == null) {
				isExistingCountry = false;
			} else {
				isExistingCountry = true;
			}

			if (isDeleteCountryRequest) {
				System.out.println("Delete country by code: " + countryCode);
				countriesDAO.deleteCountryByCode(countryCode);

				// --------------------------------------------------------
				// display page with list of all countries
				// --------------------------------------------------------
				String redirectUrl = "http://localhost:8080/DBServlet/CountriesServlet";

				System.out.println("Redirect to: " + redirectUrl);

				response.setStatus(response.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", redirectUrl);
				// --------------------------------------------------------

			} else {
				// --------------------------------------------------------
				// get country object from parameters
				// --------------------------------------------------------
				Country country = new Country();
				country.setCode(countryCode);
				country.setName(countryName);
				country.setContinent(continent);

				int pop = Integer.parseInt(countryPopulation);
				country.setPopulation(pop);

				float life = Float.parseFloat(lifeExpectancy);
				country.setLifeExpectancy(life);

				// --------------------------------------------------------
				// create or update country as needed
				// --------------------------------------------------------
				if (isExistingCountry) {
					// update country
					System.out.println("Update country: " + country);
					updateCountry(country);

				} else {
					// create new country
					System.out.println("Create country: " + country);
					createCountry(country);
				}

				// --------------------------------------------------------
				// display page with new country created or updated
				// --------------------------------------------------------
				String redirectUrl = "http://localhost:8080/DBServlet/CountriesServlet?" + FORM_COUNTRY_CODE + "="
						+ country.getCode();

				System.out.println("Redirect to: " + redirectUrl);

				response.setStatus(response.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", redirectUrl);
				// --------------------------------------------------------

			}

		}

		
		if (isCityRequest) {

			// --------------------------------------------------------
			// parse request parameters for city
			// --------------------------------------------------------
			//String cityId = request.getParameter(FORM_CITY_ID);
			String cityName = request.getParameter(FORM_CITY_NAME);
			//String countryCode = request.getParameter(FORM_CITY_COUNTRY_CODE);
			String cityPopulation = request.getParameter(FORM_CITY_POPULATION);
			// --------------------------------------------------------

			// --------------------------------------------------------
			// check if the request is to delete city or not
			// --------------------------------------------------------
			boolean isDeleteCityRequest = this.isDeleteCityRequest(request);
			System.out.println("isDeleteCityRequest: "
					+ isDeleteCityRequest);

			// --------------------------------------------------------
			// check if city exists for ....
			// --------------------------------------------------------
			boolean isExistingCity = countriesDAO.isExistingCity(cityId);
			System.out.println("isExistingCity: " + isExistingCity);
			

			if (isDeleteCityRequest) {
				
				System.out.println("isDeleteCityRequest: " + cityId);
				
				City city = countriesDAO.getCityByID(cityId);
			    cityCountryCode = city.getCountryCode();
				countriesDAO.deleteCityByID(cityId);

				// --------------------------------------------------------
				// display page with list of cities  
				// --------------------------------------------------------
				String redirectUrl = "http://localhost:8080/DBServlet/CountriesServlet?" + FORM_COUNTRY_CODE + "="
				+ cityCountryCode;						
												

				response.setStatus(response.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", redirectUrl);
				// --------------------------------------------------------

			} else {
				// --------------------------------------------------------
				// create city object from parameters
				// --------------------------------------------------------
				City city = new City();
				city.setName(cityName);
				city.setCountryCode(cityCountryCode);
				
				int pop = Integer.parseInt(cityPopulation);
				city.setPopulation(pop);

				// --------------------------------------------------------
				// create or update city as needed
				// --------------------------------------------------------
				if (isExistingCity) {
					// update city
					int id = Integer.parseInt(cityId);
					city.setID(id);

					System.out.println("updateCity: " + city);
					updateCity(city);

				} else {
					// create new city
					System.out.println("createCity: " + city);
					int createdCityID = createCity(city);
					System.out.println("createdCity: CityId=" + createdCityID);
					
					city.setID(createdCityID);
				}

				// --------------------------------------------------------
				// display page with the new city created or city updated
				// --------------------------------------------------------
				String redirectUrl = "http://localhost:8080/DBServlet/CountriesServlet?" + FORM_CITY_ID + "="
						+ city.getID();

				response.setStatus(response.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", redirectUrl);
				// --------------------------------------------------------
			}

		}
		
		
		System.out.println("----------------------- POST DONE ----------------");

	}

	private void updateCity(City city) {
		this.countriesDAO.updateCity(city);
	}

	private int createCity(City city) {
		int cityId = this.countriesDAO.createCity(city);
	    return cityId;
	}

	private void updateCountry(Country country) {
		this.countriesDAO.updateCountry(country);
	}

	private void createCountry(Country country) {
		this.countriesDAO.createCountry(country);

	}
}
