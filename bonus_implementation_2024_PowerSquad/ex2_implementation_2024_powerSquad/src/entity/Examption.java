package entity;

import java.util.Objects;

public class Examption {
	
	private int courseId;
	private int remedialCourseId;
	private int gradeLevel;
	private Boolean exampt;
	public Examption(int courseId, int remedialCourseId, int gradeLevel, Boolean exampt) {
		super();
		this.courseId = courseId;
		this.remedialCourseId = remedialCourseId;
		this.gradeLevel = gradeLevel;
		this.exampt = exampt;
	}
	@Override
	public int hashCode() {
		return Objects.hash(courseId, exampt, gradeLevel, remedialCourseId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Examption other = (Examption) obj;
		return courseId == other.courseId && Objects.equals(exampt, other.exampt) && gradeLevel == other.gradeLevel
				&& remedialCourseId == other.remedialCourseId;
	}
	@Override
	public String toString() {
		return "Examption [courseId=" + courseId + ", remedialCourseId=" + remedialCourseId + ", gradeLevel="
				+ gradeLevel + ", exampt=" + exampt + "]";
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public int getRemedialCourseId() {
		return remedialCourseId;
	}
	public void setRemedialCourseId(int remedialCourseId) {
		this.remedialCourseId = remedialCourseId;
	}
	public int getGradeLevel() {
		return gradeLevel;
	}
	public void setGradeLevel(int gradeLevel) {
		this.gradeLevel = gradeLevel;
	}
	public Boolean getExampt() {
		return exampt;
	}
	public void setExampt(Boolean exampt) {
		this.exampt = exampt;
	}
}
