package entity;


import java.util.ArrayList;
import java.util.Objects;


public class Institution {
	
	private String institutionName;
	private String institutionCountry ;
	private String city;
	private String streetAddress;
	private ArrayList <Department> deps;
	
	

	
	public Institution(String institutionName, String institutionCountry, String city, String streetAddress) {
		super();
		this.institutionName = institutionName;
		this.institutionCountry = institutionCountry;
		this.city = city;
		this.streetAddress = streetAddress;
		this.deps = new ArrayList<>();
	}


	public ArrayList<Department> getDeps() {
		return deps;
	}


	public void setDeps(ArrayList<Department> deps) {
		this.deps = deps;
	}

	 public void addDepartment(Department dept) {
	        this.deps.add(dept);
	 }

	public String getInstitutionName() {
		return institutionName;
	}
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}
	public String getInstitutionCountry() {
		return institutionCountry;
	}
	public void setInstitutionCountry(String institutionCountry) {
		this.institutionCountry = institutionCountry;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	
	
	


	@Override
	public int hashCode() {
		return Objects.hash(city, deps, institutionCountry, institutionName, streetAddress);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Institution other = (Institution) obj;
		return Objects.equals(city, other.city) && Objects.equals(deps, other.deps)
				&& Objects.equals(institutionCountry, other.institutionCountry)
				&& Objects.equals(institutionName, other.institutionName)
				&& Objects.equals(streetAddress, other.streetAddress);
	}


	@Override
	public String toString() {
		return "Institution [institutionName=" + institutionName + ", institutionCountry=" + institutionCountry
				+ ", city=" + city + ", streetAddress=" + streetAddress + ", deps=" + deps + "]";
	}
	
	
	
	
	
	
	

	
	

}
