package countries;

public class City {

	private static String class_name = "My City";
	
	public static String getClassName() {
		return class_name;
	}
	
	public String getCityDescription() {
		return class_name + ": " + cityName;
	}
	
	private int id;
	private String cityName;	
	private String countryCode;
	private int cityPopulation;
	
	public int getID() {
		return id;
	}
	public void setID(int iD) {
		id = iD;
	}
	public  String getName() {
		return cityName;
	}
	public  void setName(String name) {
		this.cityName = name;
	}
	public  String getCountryCode() {
		return countryCode;
	}
	public  void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public  int getPopulation() {
		return cityPopulation;
	}
	public  void setPopulation(int population) {
		this.cityPopulation = population;
	}
	
	@Override
	public String toString() {
		return "City [ID=" + id + ", name=" + cityName + ", countryCode="
				+ countryCode + ", population=" + cityPopulation + "]";
	}

}
