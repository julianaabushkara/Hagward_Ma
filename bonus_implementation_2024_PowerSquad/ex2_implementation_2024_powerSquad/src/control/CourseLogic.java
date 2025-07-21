package control;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import entity.Applicant;
import entity.Consts;
import entity.Course;
import entity.Examption;
import entity.InternalCourse;
import entity.RemedialCourse;

public class CourseLogic {

	
	private static CourseLogic instance;

	private CourseLogic() {
	}

	public static CourseLogic getInstance() {
		if (instance == null)
			instance = new CourseLogic();
		return instance;
	}
	
	//results.add(new Examption(rs.getInt(i++), rs.getInt(i++), rs.getInt(i++),rs.getBoolean(i++)));

	
	
	public HashMap<Integer,Course> getCourse() {
		ArrayList<Course> results = new ArrayList<Course>();
		HashMap<Integer,Course> courses=new HashMap<>();

		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_COURSES);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					results.add(new Course(rs.getInt(i++), rs.getString(i++), rs.getInt(i++)));
				}
				
				/*System.out.println("---------------------------------courses---------------------------------------------------");*/
				for(Course r:results)
				{
					courses.put(r.getCourseId(), r);
					//System.out.println(" "+r);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return courses;
	}
	

	/*	private int weeklyHours- String courseWebLink-String departmentName- String institutionName*/
	public HashMap<Integer,InternalCourse> getInternalCourses() {
		ArrayList<InternalCourse> results = new ArrayList<InternalCourse>();
		HashMap<Integer,Course> course = getCourse();
		HashMap<Integer,InternalCourse> internalCourses=new HashMap<>();
		
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_INTERNAL_COURSES);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int i = 1;
	                int courseId = rs.getInt(i++);
	                Course c=course.get(courseId);
					results.add(new InternalCourse(c.getCourseId(),c.getCourseName(),c.getCreditPoint(),rs.getInt(i++),rs.getString(i++),rs.getString(i++), rs.getString(i++)));
				}
				//System.out.println("---------------------------------internal Courses---------------------------------------------------");

				for(InternalCourse r:results)
				{
					internalCourses.put(r.getCourseId(), r);
					
					//System.out.println(""+r);

				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return internalCourses;
	}
	
	
	
	public HashMap<Integer,RemedialCourse> getRemedialCourses() {
		ArrayList<RemedialCourse> results = new ArrayList<RemedialCourse>();
		HashMap<Integer,InternalCourse> course = getInternalCourses();
		HashMap<Integer,RemedialCourse> remedialCourse=new HashMap<>();
		/*int minRequiredGrade- Boolean semesterA- Boolean semesterB -Boolean semesterSummer*/
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_REMEDIAL_COURSES);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int i = 1;
	                int courseId = rs.getInt(i++);
	                InternalCourse c=course.get(courseId);
	                /*int weeklyHours- String courseWebLink-String departmentName-String institutionName*/
					results.add(new RemedialCourse(c.getCourseId(),c.getCourseName(),c.getCreditPoint(),c.getWeeklyHours(),c.getCourseWebLink(),c.getDepartmentName(),c.getInstitutionName(), rs.getInt(i++),rs.getBoolean(i++),rs.getBoolean(i++),rs.getBoolean(i++)));
				}
				//System.out.println("--------------------------remedial Courses----------------------------------------------------------");

				for(RemedialCourse r:results)
				{
					remedialCourse.put(r.getCourseId(), r);
					//System.out.println(""+r);

				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return remedialCourse;
	}
	
	/////////////////////////////////////////////////////////////does the exemption already exsits
	
	public Boolean ExemptionAlreadyExists(int courseId,int remedialId) {
		Boolean fieldsValid = false;
		HashMap<Integer, Examption> ex = getExampt();
		fieldsValid = ex.containsKey(courseId+remedialId);
		return fieldsValid;
	}
	
	//////////////////////////////////////////////////////////get examption 

	public HashMap<Integer,Examption> getExampt() {
		ArrayList<Examption> results = new ArrayList<Examption>();
		//courseID+remedialCourseId
		HashMap<Integer,Examption> examption=new HashMap<>();

		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_EXAMPT_COURSES);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					results.add(new Examption(rs.getInt(i++),rs.getInt(i++),rs.getInt(i++), rs.getBoolean(i++)));
				}
				
				//System.out.println("---------------------------------Examption---------------------------------------------------");
				for(Examption r:results)
				{
					examption.put(r.getCourseId()+r.getRemedialCourseId(), r);
					//System.out.println(" "+r);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return examption;
	}
	
	
	
	
	public boolean editExemption(int courseId, int remedialID, int grade, Boolean exampt) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_UPD_EXAMPT_COURSES)) {
				
				int i = 1;
				
				stmt.setInt(i++, grade); // can't be null
				stmt.setBoolean(i++, exampt); // can't be null
				
				
				stmt.setInt(i++, courseId);
				stmt.setInt(i++, remedialID);// can't be null
				
				stmt.executeUpdate();
				return true;
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public boolean addExamption(int courseId, int remedialCourseId, int grade, Boolean exampt) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_INS_EXAMPT_COURSES)) {
				int i = 1;

				stmt.setInt(i++, courseId); // can't be null
				stmt.setInt(i++, remedialCourseId); // can't be null
				stmt.setInt(i++, grade); // can't be null
				stmt.setBoolean(i++, exampt); // can't be null

				
				stmt.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	
	
	public boolean removeExemption(int courseId, int remedialId) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt1 = conn.prepareCall(Consts.SQL_DEL_EXAMPT_COURSES)) {
				stmt1.setInt(1, courseId);
				stmt1.setInt(2, remedialId);
				stmt1.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
}
