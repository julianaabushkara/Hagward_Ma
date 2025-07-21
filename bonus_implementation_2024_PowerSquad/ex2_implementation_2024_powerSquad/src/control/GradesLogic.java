package control;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import entity.Applicant;
import entity.ApplicantDocument;
import entity.ApplicantStatus;
import entity.BackgroundEducation;
import entity.Consts;
import entity.GotGrade;
import entity.PassingGrade;
import entity.Stage;




public class GradesLogic {

	private static GradesLogic instance;

	private GradesLogic() {
	}

	public static GradesLogic getInstance() {
		if (instance == null)
			instance = new GradesLogic();
		return instance;
	}
	
	public HashMap<String,GotGrade> getGotGrade() {
		ArrayList<GotGrade> results = new ArrayList<GotGrade>();
		HashMap<String,GotGrade>grades=new HashMap<String,GotGrade>();
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_GOT_GRADES);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					results.add(new GotGrade(rs.getString(i++), rs.getInt(i++), rs.getDouble(i++)));
				}
				
				for(GotGrade g :results)
				{
					grades.put(g.getId()+g.getCourseId(), g);
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return grades;
	}

	
	public HashMap<String,PassingGrade> getPassingGrade() {
		ArrayList<PassingGrade> results = new ArrayList<PassingGrade>();
		HashMap<String,PassingGrade> grades=new HashMap<String,PassingGrade>();
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_PASSING_GRADES);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					results.add(new PassingGrade(rs.getString(i++), rs.getInt(i++), rs.getDouble(i++)));
				}
				for (PassingGrade p : results) {

					grades.put(p.getApplicantId()+p.getRemedialCourseId(), p);
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return grades;
	}
	
	  
    /////////////////////////////////////////////////////////////////////////////////////////////
    //adds passing Grade
    ///////////////////////////////////////////////////////////////////////////////////////////
   
    public boolean addPassingGrade(String applicantId, int courseId,Double grade) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                    CallableStatement stmt = conn.prepareCall(Consts.SQL_INS_PASSING_GRADE)) {
                int i = 1;

                stmt.setString(i++, applicantId);  // can't be null
                stmt.setInt(i++, courseId);
                stmt.setDouble(i++, grade);

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
    
    
    public boolean editPassingGrade(String applicantID, int courseId, Double grade) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_UPD_PASSING_GRADE)) {
				int i = 1;
				stmt.setDouble(i++, grade);
				stmt.setString(i++, applicantID); // can't be null
				stmt.setInt(i++, courseId); // can't be null
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
    

	public boolean removePassingGrade(String applicantId, int remedialId) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt1 = conn.prepareCall(Consts.SQL_DELETE_PASSING_GRADE)) {
				stmt1.setString(1, applicantId);
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
	
	

	
	
	public Boolean GradeAlreadyExists(GotGrade g)
	{
		HashMap<String,GotGrade>grades=getGotGrade();
		Boolean b=false;
		b=grades.containsKey(g.getId()+g.getCourseId());
		return b;	
	}
	
	
	 public void importGradesFromJSON(String path) {
	    	try (FileReader reader = new FileReader(new File(path))) {
	    		JsonObject doc = (JsonObject) Jsoner.deserialize(reader);
	    		JsonArray customers = (JsonArray) doc.get("Applicants_Grades_info");
	    		Iterator<Object> iterator = customers.iterator();
	    		int errors = 0;
	    		while (iterator.hasNext()) {
	    			JsonObject obj = (JsonObject) iterator.next();
	    			
	    			String id=(String) obj.get("ApplicantId");
	    			String courseID=(String) obj.get("RemedialCourseId");
	    			String grade=(String) obj.get("Grade");

	    			
	    			int courseId=Integer.parseInt(courseID);
	    			Double gotGrade=Double.parseDouble(grade);
	    			GotGrade c=new GotGrade(id,courseId,gotGrade);
	    			Boolean exist=GradeAlreadyExists(c);
	    			Boolean Success=false;
	    			if(!exist)
	    			{
	    				Success=addGotGrade(c.getId(),c.getCourseId(),c.getGrade());
	    				//System.out.println(" in insert Success");

	    			}else 
	    			{
	    				Success=editGrade(c.getId(),c.getCourseId(),c.getGrade());
	    				//System.out.println(" in edit Success");

	    				
	    			}
	    			//if (Success)
	    				//System.out.println(" Success");
	    		}
	    		
	    	} catch (IOException | DeserializationException e) {
	    		e.printStackTrace();
	    	}
	    }
	 
	 
	 
	 public boolean editGrade(String ID, int courseID, Double grade) {
			try {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
						CallableStatement stmt = conn.prepareCall(Consts.SQL_UPD_GOT_GRADE)) {
					int i = 1;

					stmt.setDouble(i++, grade);

					stmt.setString(i++, ID); // can't be null
					stmt.setInt(i++, courseID); // can't be null
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
	 
	 
		public boolean addGotGrade(String ID, int course, Double grade ) {

			try {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
						CallableStatement stmt = conn.prepareCall(Consts.SQL_INS_GOT_GRADE)) {

					int i = 1;
					stmt.setString(i++, ID); // can't be null
					stmt.setInt(i++, course);
					stmt.setDouble(i++, grade); // can't be null
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
	
	
	
	public List<Applicant> checkCondition(){
		List<Applicant>expelledStudents=new ArrayList<>();
		HashMap<String,Applicant>applicants=ApplicantLogic.getInstance().getApplicants();
		HashMap<String,GotGrade> gotGrades=getGotGrade();
		HashMap<String,PassingGrade>passingGrade=getPassingGrade();
		
		for(GotGrade g:gotGrades.values())
		{
			Double currentGrade=g.getGrade();
			String id=g.getId();
			int courseId=g.getCourseId();
			Double requiredGrade=0.0;
			if(passingGrade.containsKey(id+courseId))
			{
				requiredGrade=passingGrade.get(id+courseId).getPassingGrade();
				if(requiredGrade>currentGrade)
				{
					expelledStudents.add(applicants.get(g.getId()));
				}
			}
		}
		return expelledStudents;
	}
	
	
}
