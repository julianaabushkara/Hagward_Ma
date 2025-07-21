package entity;

import java.util.Objects;

public class Department {

	private String departmentName;
	private String institutionName;

	
	// info system - haifa 052458954123
	//info system - tel aviv -0256485697125
	private String departmentPhoneNumber;


	public Department(String departmentName, String institutionName, String departmentPhoneNumber) {
		super();
		this.departmentName = departmentName;
		this.institutionName = institutionName;
		this.departmentPhoneNumber = departmentPhoneNumber;
	}


	@Override
	public int hashCode() {
		return Objects.hash(departmentName, departmentPhoneNumber, institutionName);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		return Objects.equals(departmentName, other.departmentName)
				&& Objects.equals(departmentPhoneNumber, other.departmentPhoneNumber)
				&& Objects.equals(institutionName, other.institutionName);
	}


	public String getDepartmentName() {
		return departmentName;
	}


	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}


	public String getInstitutionName() {
		return institutionName;
	}


	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}


	public String getDepartmentPhoneNumber() {
		return departmentPhoneNumber;
	}


	public void setDepartmentPhoneNumber(String departmentPhoneNumber) {
		this.departmentPhoneNumber = departmentPhoneNumber;
	}


	@Override
	public String toString() {
		return "Department [departmentName=" + departmentName + ", institutionName=" + institutionName
				+ ", departmentPhoneNumber=" + departmentPhoneNumber + "]";
	}
	
	
	
	
	
	
}
