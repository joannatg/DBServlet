package countries;

public class Country {

	private String code;
	private String name;	
	private String continent;
	private int population;
	private float lifeExpectancy;
	
	public float getLifeExpectancy() {
		return lifeExpectancy;
	}
	public void setLifeExpectancy(float lifeExpectancy) {
		this.lifeExpectancy = lifeExpectancy;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContinent() {
		return continent;
	}
	public void setContinent(String continent) {
		this.continent = continent;
	}
	public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
	}
	@Override
	public String toString() {
		return "Country [code=" + code + ", name=" + name + ", continent="
				+ continent + ", population=" + population
				+ ", lifeExpectancy=" + lifeExpectancy + "]";
	}
	
}
