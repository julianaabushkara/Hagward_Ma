package entity;

import java.util.Objects;

public class Course {

	private int courseId;
	private String courseName;
	private int creditPoint;
	
	
	////// constructor to download the courses from the data base -> get

	public Course(int courseId, String courseName, int creditPoint) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
		this.creditPoint = creditPoint;
	}


	@Override
	public int hashCode() {
		return Objects.hash(courseId, courseName, creditPoint);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		return courseId == other.courseId && Objects.equals(courseName, other.courseName)
				&& creditPoint == other.creditPoint;
	}


	public int getCourseId() {
		return courseId;
	}


	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}


	public String getCourseName() {
		return courseName;
	}


	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	public int getCreditPoint() {
		return creditPoint;
	}


	public void setCreditPoint(int creditPoint) {
		this.creditPoint = creditPoint;
	}


	@Override
	public String toString() {
		return "Course [courseId=" + courseId + ", courseName=" + courseName + ", creditPoint=" + creditPoint + "]";
	}
	
	
	

}
