package countries;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountriesDAO {

	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

	public static final String DATABASE_NAME = "world";
	public static final String URL = "jdbc:mysql://localhost:3306/"
			+ DATABASE_NAME;
	public static final String USER = "root";
	public static final String PASSWORD = "nimda";

	public CountriesDAO() {

	}

	public List<Country> getAllCountries() {
		List<Country> countries = new ArrayList<Country>();

		ResultSet rs = null;
		String query = "SELECT * FROM country order by Code ASC";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();)

		{
			rs = statement.executeQuery(query);
			countries = createCountriesFromResultSet(rs);

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return countries;
	}

	/**
	 * Creates Country From ResultSet.
	 * 
	 * @param rs
	 *            the ResultSet
	 * @return new country created from ResultSet
	 * @throws SQLException
	 */
	public Country createCountryFromResultSet(ResultSet rs) throws SQLException {
		Country c = new Country();
		c.setCode(rs.getString("Code"));
		c.setName(rs.getString("Name"));
		c.setContinent(rs.getString("Continent"));
		c.setPopulation(rs.getInt("Population"));
		c.setLifeExpectancy(rs.getFloat("LifeExpectancy"));
		return c;
	}

	public List<City> getCitiesByCountryCode(String code) {
		List<City> cities = null;

		String query = "SELECT * FROM city where CountryCode = '" + code + "'";

		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();) {

			ResultSet rs = statement.executeQuery(query);
			cities = createCitiesFromResultSet(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cities;
	}

	public List<Country> getCountriesByName(String name) {
		List<Country> countries = null;

		String query = "SELECT * FROM country where Name like \"" + name
				+ "%\"";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();)

		{
			ResultSet rs = statement.executeQuery(query);
			countries = createCountriesFromResultSet(rs);

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return countries;
	}

	public List<City> createCitiesFromResultSet(ResultSet rs)
			throws SQLException {

		List<City> cities = new ArrayList<City>();
		while (rs.next()) {
			City city = createCityFromResultSet(rs);
			cities.add(city);
		}
		return cities;
	}

	public int createCity(City city) {

		int id = -1;

		// ResultSet rs = null;
		String query = "INSERT INTO city (Name, CountryCode, Population) VALUES ('"
				+ city.getName()
				+ "', '"
				+ city.getCountryCode()
				+ "', "
				+ city.getPopulation() + ")";

		System.out.println("Executing query: " + query);

		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement stmt = connection.createStatement();) {

			stmt.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		City createdCity = this.getCityByNameInCountry(city.getName(),city.getCountryCode());
		id = createdCity.getID();

		return id;

	}

	public City getCityByNameInCountry(String cityName, String cityCountryCode) {
		City city = null;

		if (cityName == null) {
			return null;
		}
		if (cityCountryCode == null) {
			return null;
		}

		String query = "SELECT * FROM city where Name = \"" + cityName	+ "\" and CountryCode = \"" + cityCountryCode + "\"";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();) {

			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				city = createCityFromResultSet(rs);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return city;
	}

	public City getCityByID(String cityId) {
		City city = null;

		if (cityId == null) {
			return null;
		}
		String query = "SELECT * FROM city where ID = \"" + cityId + "\"";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();) {

			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				city = createCityFromResultSet(rs);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return city;
	}

	public City createCityFromResultSet(ResultSet rs) throws SQLException {
		City city = new City();
		city.setID(rs.getInt("ID"));
		city.setName(rs.getString("Name"));
		city.setCountryCode(rs.getString("CountryCode"));
		city.setPopulation(rs.getInt("Population"));
		return city;
	}

	/**
	 * @param rs
	 * @throws SQLException
	 */
	public List<Country> createCountriesFromResultSet(ResultSet rs)
			throws SQLException {
		List<Country> countries = new ArrayList<Country>();
		while (rs.next()) {
			Country c = createCountryFromResultSet(rs);
			countries.add(c);
		}
		return countries;
	}

	public void deleteCountryByCode(String code) {

		String query = "DELETE from country WHERE Code = '" + code + "'";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement stmt = connection.createStatement();) {

			stmt.executeUpdate(query);

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public Country getCountryByCode(String code) {
		Country country = null;

		if (code == null) {
			return null;
		}

		String query = "SELECT * FROM country where Code = \"" + code + "\"";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();) {

			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				country = createCountryFromResultSet(rs);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return country;
	}

	public void updateCity(City city) {
		String query = "UPDATE city SET Name= '" + city.getName()	+ "', CountryCode= '" + city.getCountryCode()
				+ "', Population='" + city.getPopulation() 
				+ "' WHERE ID = '"
				+ city.getID() + "'";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement stmt = connection.createStatement();) {

			stmt.executeUpdate(query);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void deleteCityByID(String cityId) {

		String query = "DELETE from city WHERE ID = '" + cityId + "'";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement stmt = connection.createStatement();) {

			stmt.executeUpdate(query);

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public boolean isExistingCity(String id) {
		return (this.getCityByID(id) != null);
	}

	public void updateCountry(Country country) {

		String query = "UPDATE country SET Name= '" + country.getName()
				+ "', Continent= '" + country.getContinent()
				+ "', Population='" + country.getPopulation()
				+ "', LifeExpectancy='" + country.getLifeExpectancy()
				+ "' WHERE Code = '" + country.getCode() + "'";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement stmt = connection.createStatement();) {

			stmt.executeUpdate(query);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void createCountry(Country country) {

		// ResultSet rs = null;
		String query = "INSERT INTO country (Code, Name, Continent, Population, LifeExpectancy) VALUES ('"
				+ country.getCode()
				+ "', '"
				+ country.getName()
				+ "', '"
				+ country.getContinent()
				+ "', "	+ country.getPopulation()	+ ", " + country.getLifeExpectancy() + ")";

		System.out.println("Executing query: " + query);

		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement stmt = connection.createStatement();) {

			stmt.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void testDatabaseAccess() {

		String query = "SELECT * from person";
		try (Connection connection = DriverManager.getConnection(URL, USER,
				PASSWORD); Statement statement = connection.createStatement();)

		{
			Class.forName(DRIVER_CLASS);
			System.out.println(connection.getCatalog());

			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				System.out.println("id=" + rs.getInt("id"));
				System.out.println("firstname=" + rs.getString("firstname"));
				System.out.println();
			}

			// DELETE
			String sqlDelete = "DELETE from person where firstname = 'Mark' and lastname = 'Twain'";
			System.out.println("The SQL query is: + sqlDelete");
			int countDeleted = statement.executeUpdate(sqlDelete);
			System.out.println(countDeleted + " records deleted.\n");
			// rs = statement.executeQuery(sqlDelete);

			// INSERT
			String sqlInsert = "INSERT into person (firstname, lastname) values "
					+ "('Mark', 'Twain'),"
					+ "('Bill', 'Hill'),"
					+ "('Mary', 'Grey')";
			System.out.println("The SQL query is: + sqlInsert");
			int countInserted = statement.executeUpdate(sqlInsert);
			System.out.println(countInserted + "records inserted.\n");

			// Issue SELECT to check the changes
			String strSelect = "SELECT * from person";
			System.out.println("The SQL query is: " + strSelect);
			rs = statement.executeQuery(strSelect);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public static void testGetCitiesFromCountry() {
		String code = "POL";

		CountriesDAO dao = new CountriesDAO();

		// Print cities for country
		System.out
				.println("---------------------------------------------------");
		boolean isError = false;
		System.out.println("Print cities for country with code=" + code);
		List<City> cities = dao.getCitiesByCountryCode(code);
		for (City city : cities) {
			System.out.println(" o-> city: " + city);
			if (!city.getCountryCode().equals(code)) {
				isError = true;
				System.out
						.println("=====>  Error **********************************************");
			}

		}
		System.out
				.println("---------------------------------------------------");
		System.out.println("The number of cities: " + cities.size());
		System.out.println("isError=" + isError);

		System.out.println();
	}

	public static void testGetCityInfoFromCityName() {
		String name = "Poznan";
		CountriesDAO dao = new CountriesDAO();
		// print info for a city
		boolean isError = false;
		System.out.println("Print city info from city name: " + name);
		System.out
				.println("---------------------------------------------------");
		City city = dao.getCityByID(name);
		System.out.println(" o-> city: " + city);
		if (!city.getName().equals(name)) {
			isError = true;
			System.out
					.println("=====>  Error **********************************************");
		}
		System.out
				.println("---------------------------------------------------");

	}

	public static void main(String[] args) {
		testDatabaseAccess();
		// testGetCitiesFromCountry();
		// testGetCityInfoFromCityName();

	}

}
