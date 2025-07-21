package entity;

import java.util.Objects;

public class InternalCourse extends Course {

	
	private int weeklyHours;
	private String courseWebLink;
	private String departmentName;
	private String institutionName;	
	
	public InternalCourse(int courseId, String courseName, int creditPoint, int weeklyHours, String courseWebLink,
			String departmentName, String institutionName) {
		super(courseId, courseName, creditPoint);
		this.weeklyHours = weeklyHours;
		this.courseWebLink = courseWebLink;
		this.departmentName = departmentName;
		this.institutionName = institutionName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(courseWebLink, departmentName, institutionName, weeklyHours);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		InternalCourse other = (InternalCourse) obj;
		return Objects.equals(courseWebLink, other.courseWebLink)
				&& Objects.equals(departmentName, other.departmentName)
				&& Objects.equals(institutionName, other.institutionName) && weeklyHours == other.weeklyHours;
	}

	public int getWeeklyHours() {
		return weeklyHours;
	}

	public void setWeeklyHours(int weeklyHours) {
		this.weeklyHours = weeklyHours;
	}

	public String getCourseWebLink() {
		return courseWebLink;
	}

	public void setCourseWebLink(String courseWebLink) {
		this.courseWebLink = courseWebLink;
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

	@Override
	public String toString() {
		return "InternalCourse [weeklyHours=" + weeklyHours + ", courseWebLink=" + courseWebLink + ", departmentName="
				+ departmentName + ", institutionName=" + institutionName + "]";
	}
	
	





	
	

}
