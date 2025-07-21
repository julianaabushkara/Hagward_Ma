package entity;

import java.util.Objects;

public class GotGrade {
	
	private String id;
	private int courseId;
	private Double Grade;
	public GotGrade(String id, int courseId, Double grade) {
		super();
		this.id = id;
		this.courseId = courseId;
		Grade = grade;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public Double getGrade() {
		return Grade;
	}
	public void setGrade(Double grade) {
		Grade = grade;
	}
	@Override
	public int hashCode() {
		return Objects.hash(Grade, courseId, id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GotGrade other = (GotGrade) obj;
		return Objects.equals(Grade, other.Grade) && courseId == other.courseId && Objects.equals(id, other.id);
	}
	
	

}
