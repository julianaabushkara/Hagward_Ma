package entity;

import java.util.Objects;

public class RemedialCourse extends InternalCourse  {
	
	
	private int minRequiredGrade;
	private Boolean semesterA ;
	private Boolean semesterB ;
	private Boolean semesterSummer ;
	public RemedialCourse(int courseId, String courseName, int creditPoint, int weeklyHours, String courseWebLink,
			String departmentName, String institutionName, int minRequiredGrade, Boolean semesterA, Boolean semesterB,
			Boolean semesterSummer) {
		super(courseId, courseName, creditPoint, weeklyHours, courseWebLink, departmentName, institutionName);
		this.minRequiredGrade = minRequiredGrade;
		this.semesterA = semesterA;
		this.semesterB = semesterB;
		this.semesterSummer = semesterSummer;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(minRequiredGrade, semesterA, semesterB, semesterSummer);
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
		RemedialCourse other = (RemedialCourse) obj;
		return minRequiredGrade == other.minRequiredGrade && Objects.equals(semesterA, other.semesterA)
				&& Objects.equals(semesterB, other.semesterB) && Objects.equals(semesterSummer, other.semesterSummer);
	}
	public int getMinRequiredGrade() {
		return minRequiredGrade;
	}
	public void setMinRequiredGrade(int minRequiredGrade) {
		this.minRequiredGrade = minRequiredGrade;
	}
	public Boolean getSemesterA() {
		return semesterA;
	}
	public void setSemesterA(Boolean semesterA) {
		this.semesterA = semesterA;
	}
	public Boolean getSemesterB() {
		return semesterB;
	}
	public void setSemesterB(Boolean semesterB) {
		this.semesterB = semesterB;
	}
	public Boolean getSemesterSummer() {
		return semesterSummer;
	}
	public void setSemesterSummer(Boolean semesterSummer) {
		this.semesterSummer = semesterSummer;
	}
	@Override
	public String toString() {
		return "RemedialCourse [minRequiredGrade=" + minRequiredGrade + ", semesterA=" + semesterA + ", semesterB="
				+ semesterB + ", semesterSummer=" + semesterSummer + "]";
	}
	
	
	

	
	

}
