package entity;

import java.util.Objects;

public class ExternalCourse extends Course{
	
	private String institutionName ;
	private String syllabusLink;
	public ExternalCourse(int courseId, String courseName, int creditPoint, String institutionName,
			String syllabusLink) {
		super(courseId, courseName, creditPoint);
		this.institutionName = institutionName;
		this.syllabusLink = syllabusLink;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(institutionName, syllabusLink);
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
		ExternalCourse other = (ExternalCourse) obj;
		return Objects.equals(institutionName, other.institutionName)
				&& Objects.equals(syllabusLink, other.syllabusLink);
	}
	public String getInstitutionName() {
		return institutionName;
	}
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}
	public String getSyllabusLink() {
		return syllabusLink;
	}
	public void setSyllabusLink(String syllabusLink) {
		this.syllabusLink = syllabusLink;
	}
	@Override
	public String toString() {
		return "ExternalCourse [institutionName=" + institutionName + ", syllabusLink=" + syllabusLink + "]";
	}
	
	
	
	
	

}
