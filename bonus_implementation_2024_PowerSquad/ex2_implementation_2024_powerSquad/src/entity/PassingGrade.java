package entity;

import java.util.Objects;

public class PassingGrade {
	
	private String applicantId;
	private int RemedialCourseId;
	private Double PassingGrade;
	
	
	public PassingGrade(String applicantId, int remedialCourseId, Double passingGrade) {
		super();
		this.applicantId = applicantId;
		RemedialCourseId = remedialCourseId;
		PassingGrade = passingGrade;
	}


	@Override
	public int hashCode() {
		return Objects.hash(PassingGrade, RemedialCourseId, applicantId);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PassingGrade other = (PassingGrade) obj;
		return Objects.equals(PassingGrade, other.PassingGrade) && RemedialCourseId == other.RemedialCourseId
				&& Objects.equals(applicantId, other.applicantId);
	}


	@Override
	public String toString() {
		return "PassingGrade [applicantId=" + applicantId + ", RemedialCourseId=" + RemedialCourseId + ", PassingGrade="
				+ PassingGrade + "]";
	}


	public String getApplicantId() {
		return applicantId;
	}


	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}


	public int getRemedialCourseId() {
		return RemedialCourseId;
	}


	public void setRemedialCourseId(int remedialCourseId) {
		RemedialCourseId = remedialCourseId;
	}


	public Double getPassingGrade() {
		return PassingGrade;
	}


	public void setPassingGrade(Double passingGrade) {
		PassingGrade = passingGrade;
	}
}
