package entity;

import java.sql.Date;
import java.util.Objects;

public class BackgroundEducation {
	
	private String applicantId;
	private String departmentname;
	private String instutionName;
	private Date graduationDate;

	private int creditPoints;
	private double averageGrade;
	public BackgroundEducation(  String instutionName, String departmentname,String applicantId, Date graduationDate,
			double averageGrade, int creditPoints) {
		super();
		this.applicantId = applicantId;
		this.departmentname = departmentname;
		this.instutionName = instutionName;
		this.creditPoints = creditPoints;
		this.averageGrade = averageGrade;
		this.graduationDate = graduationDate;
	}
	public String getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
	public String getDepartmentname() {
		return departmentname;
	}
	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}
	public String getInstutionName() {
		return instutionName;
	}
	public void setInstutionName(String instutionName) {
		this.instutionName = instutionName;
	}
	public int getCreditPoints() {
		return creditPoints;
	}
	public void setCreditPoints(int creditPoints) {
		this.creditPoints = creditPoints;
	}
	public double getAverageGrade() {
		return averageGrade;
	}
	public void setAverageGrade(double averageGrade) {
		this.averageGrade = averageGrade;
	}
	public Date getGraduationDate() {
		return graduationDate;
	}
	public void setGraduationDate(Date graduationDate) {
		this.graduationDate = graduationDate;
	}
	@Override
	public int hashCode() {
		return Objects.hash(applicantId, averageGrade, creditPoints, departmentname, graduationDate, instutionName);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BackgroundEducation other = (BackgroundEducation) obj;
		return Objects.equals(applicantId, other.applicantId)
				&& Double.doubleToLongBits(averageGrade) == Double.doubleToLongBits(other.averageGrade)
				&& creditPoints == other.creditPoints && Objects.equals(departmentname, other.departmentname)
				&& Objects.equals(graduationDate, other.graduationDate)
				&& Objects.equals(instutionName, other.instutionName);
	}
	@Override
	public String toString() {
		return "BackgroundEducation [applicantId=" + applicantId + ", departmentname=" + departmentname
				+ ", instutionName=" + instutionName + ", creditPoints=" + creditPoints + ", averageGrade="
				+ averageGrade + ", graduationDate=" + graduationDate + "]";
	}
	
	
	
	
	
}
