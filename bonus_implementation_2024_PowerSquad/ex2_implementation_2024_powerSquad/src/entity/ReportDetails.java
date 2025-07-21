package entity;

import java.util.Objects;

public class ReportDetails {

	private String institutionName;
	private String status;
	private Double percentage;
	
	public ReportDetails(String institutionName, String status, Double percentage) {
		super();
		this.institutionName = institutionName;
		this.status = status;
		this.percentage = percentage;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	@Override
	public int hashCode() {
		return Objects.hash(institutionName, percentage, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportDetails other = (ReportDetails) obj;
		return Objects.equals(institutionName, other.institutionName) && Objects.equals(percentage, other.percentage)
				&& Objects.equals(status, other.status);
	}

	@Override
	public String toString() {
		return "ReportDetails [institutionName=" + institutionName + ", status=" + status + ", percentage=" + percentage
				+ "]";
	}
	
	

	


    
    

}
